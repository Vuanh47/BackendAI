package org.example.backendai.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.MessageType;
import org.example.backendai.dto.request.MessageRequest;
import org.example.backendai.dto.response.MessageResponse;
import org.example.backendai.entity.Message;
import org.example.backendai.service.MessageService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketEventListener {

    SimpMessagingTemplate messagingTemplate;
    MessageService messageService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("New WebSocket connection established");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        if (username != null && roomId != null) {
            log.info("User {} disconnected from room {}", username, roomId);

            try {
                // Tạo LEAVE message
                MessageRequest leaveRequest = MessageRequest.builder()
                        .senderUsername(username)
                        .content(username + " left the chat")
                        .type(MessageType.LEAVE)
                        .roomId(roomId)
                        .build();

                // Lưu và broadcast
                MessageResponse response = messageService.sendMessage(leaveRequest);
                messagingTemplate.convertAndSend("/topic/chat/" + roomId, response);

            } catch (Exception e) {
                log.error("Error sending LEAVE message for user {}", username, e);
            }
        }
    }
}