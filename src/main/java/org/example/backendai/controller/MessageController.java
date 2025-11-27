package org.example.backendai.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.MessageType;
import org.example.backendai.constant.SuccessCode;
import org.example.backendai.dto.request.MessageRequest;
import org.example.backendai.dto.response.ApiResponse;
import org.example.backendai.dto.response.MessageResponse;
import org.example.backendai.service.MessageService;
import org.example.backendai.util.ApiResponseUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageController {

    MessageService messageService;
    SimpMessagingTemplate messagingTemplate;

    // ============= WebSocket Endpoints =============

    /**
     * WebSocket: Gửi message vào room
     */
    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public MessageResponse sendMessage(
            @DestinationVariable String roomId,
            @Payload MessageRequest request) {

        log.info("WebSocket: Received message from {} to room {}",
                request.getSenderUsername(), roomId);

        request.setRoomId(roomId);
        request.setType(MessageType.CHAT);

        return messageService.sendMessage(request);
    }

    /**
     * WebSocket: User join room
     */
    @MessageMapping("/chat.addUser/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public MessageResponse addUser(
            @DestinationVariable String roomId,
            @Payload MessageRequest request,
            SimpMessageHeaderAccessor headerAccessor) {

        log.info("WebSocket: User {} joined room {}", request.getSenderUsername(), roomId);

        // Lưu thông tin vào session
        headerAccessor.getSessionAttributes().put("username", request.getSenderUsername());
        headerAccessor.getSessionAttributes().put("roomId", roomId);

        request.setRoomId(roomId);
        request.setType(MessageType.JOIN);
        request.setContent(request.getSenderUsername() + " joined the chat");

        return messageService.sendMessage(request);
    }

    /**
     * WebSocket: Private Chat (1-1) - Gửi message cho user cụ thể
     */
    @MessageMapping("/chat.privateMessage/{receiverUsername}")
    public void sendPrivateMessage(
            @DestinationVariable String receiverUsername,
            @Payload MessageRequest request) {

        log.info("WebSocket: Private message from {} to {}",
                request.getSenderUsername(), receiverUsername);

        request.setReceiverUsername(receiverUsername);
        request.setType(MessageType.CHAT);

        MessageResponse response = messageService.sendMessage(request);

        // Gửi cho receiver
        messagingTemplate.convertAndSendToUser(
                receiverUsername,
                "/queue/private",
                response
        );

        // Gửi lại cho sender để confirm
        messagingTemplate.convertAndSendToUser(
                request.getSenderUsername(),
                "/queue/private",
                response
        );

        log.info("Private message sent successfully");
    }

    // ============= REST API Endpoints =============

    /**
     * Gửi message (REST)
     */
    @PostMapping("/api/messages/send")
    @ResponseBody
    public ResponseEntity<MessageResponse> sendMessageRest(@RequestBody MessageRequest request) {
        log.info("REST: Sending message from {} to {}",
                request.getSenderUsername(), request.getReceiverUsername());

        if (request.getType() == null) {
            request.setType(MessageType.CHAT);
        }

        MessageResponse data = messageService.sendMessage(request);

        // Broadcast qua WebSocket nếu có roomId
        if (request.getRoomId() != null) {
            messagingTemplate.convertAndSend("/topic/chat/" + request.getRoomId(), data);
        }

        // Broadcast cho receiver (private chat)
        if (request.getReceiverUsername() != null) {
            messagingTemplate.convertAndSendToUser(
                    request.getReceiverUsername(),
                    "/queue/private",
                    data
            );
        }

        return ApiResponseUtil.success(data, SuccessCode.SEND_MESSAGE_SUCCESS);
    }

    /**
     * Lấy messages theo room
     */
    @GetMapping("/api/messages/room/{roomId}")
    @ResponseBody
    public ResponseEntity<List<MessageResponse>> getMessagesByRoom(@PathVariable String roomId) {
        List<MessageResponse> data = messageService.getMessagesByRoom(roomId);
        return ApiResponseUtil.success(data, SuccessCode.GET_MESSAGES_SUCCESS);
    }

    /**
     * Lấy messages giữa 2 users
     */
    @GetMapping("/api/messages/between/{username1}/{username2}")
    @ResponseBody
    public ResponseEntity<List<MessageResponse>> getMessagesBetweenUsers(
            @PathVariable String username1,
            @PathVariable String username2) {
        List<MessageResponse> data = messageService.getMessagesBetweenUsers(username1, username2);
        return ApiResponseUtil.success(data, SuccessCode.GET_MESSAGES_SUCCESS);
    }

    /**
     * Lấy messages chưa đọc
     */
    @GetMapping("/api/messages/unread/{username}")
    @ResponseBody
    public ResponseEntity<List<MessageResponse>> getUnreadMessages(@PathVariable String username) {
        List<MessageResponse> data = messageService.getUnreadMessages(username);
        return ApiResponseUtil.success(data, SuccessCode.GET_MESSAGES_SUCCESS);
    }

    /**
     * Lấy messages theo sender
     */
    @GetMapping("/api/messages/sender/{username}")
    @ResponseBody
    public ResponseEntity<List<MessageResponse>> getMessagesBySender(@PathVariable String username) {
        List<MessageResponse> data = messageService.getMessagesBySender(username);
        return ApiResponseUtil.success(data, SuccessCode.GET_MESSAGES_SUCCESS);
    }

    /**
     * Lấy danh sách rooms của user
     */
    @GetMapping("/api/messages/rooms/{username}")
    @ResponseBody
    public ResponseEntity<List<String>> getUserRooms(@PathVariable String username) {
        List<String> data = messageService.getUserRooms(username);
        return ApiResponseUtil.success(data, SuccessCode.GET_ROOMS_SUCCESS);
    }

    /**
     * Đánh dấu message đã đọc
     */
    @PutMapping("/api/messages/{messageId}/read")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Integer messageId) {
        messageService.markAsRead(messageId);
        return ApiResponseUtil.success(SuccessCode.MESSAGE_MARKED_READ);
    }

    /**
     * Đánh dấu tất cả messages đã đọc
     */
    @PutMapping("/api/messages/{doctorUsername}/{patientUsername}/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @PathVariable String doctorUsername,
            @PathVariable String patientUsername) {

        log.info("Marking messages as read between doctor: {} and patient: {}",
                doctorUsername, patientUsername);

        messageService.markAllAsReadBetweenUsers(doctorUsername, patientUsername);
        return ApiResponseUtil.success(SuccessCode.ALL_MESSAGES_MARKED_READ);
    }

    /**
     * Xóa message
     */
    @DeleteMapping("/api/messages/{messageId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Integer messageId) {
        messageService.deleteMessage(messageId);
        return ApiResponseUtil.success(SuccessCode.DELETE_MESSAGE_SUCCESS);
    }

    /**
     * Xóa tất cả messages của room
     */
    @DeleteMapping("/api/messages/room/{roomId}")
    @ResponseBody
    public ResponseEntity<ApiResponse<Void>> deleteRoomMessages(@PathVariable String roomId) {
        messageService.deleteRoomMessages(roomId);
        return ApiResponseUtil.success(SuccessCode.DELETE_MESSAGES_SUCCESS);
    }

    /**
     * Đếm số messages chưa đọc
     */
    @GetMapping("/api/messages/unread-count/{username}")
    @ResponseBody
    public ResponseEntity<Long> getUnreadCount(@PathVariable String username) {
        Long count = messageService.countUnreadMessages(username);
        return ApiResponseUtil.success(count, SuccessCode.GET_UNREAD_COUNT_SUCCESS);
    }

    /**
     * Lấy messages theo khoảng thời gian
     */
    @GetMapping("/api/messages/date-range")
    @ResponseBody
    public ResponseEntity<List<MessageResponse>> getMessagesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<MessageResponse> data = messageService.getMessagesByDateRange(start, end);
        return ApiResponseUtil.success(data, SuccessCode.GET_MESSAGES_SUCCESS);
    }
}