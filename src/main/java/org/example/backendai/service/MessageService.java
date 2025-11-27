package org.example.backendai.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.backendai.constant.ErrorCode;
import org.example.backendai.dto.request.MessageRequest;
import org.example.backendai.dto.response.MessageResponse;
import org.example.backendai.entity.Message;
import org.example.backendai.entity.MessageClassification;
import org.example.backendai.entity.User;
import org.example.backendai.exception.AppException;
import org.example.backendai.mapper.MessageMapper;
import org.example.backendai.repository.MessageClassificationRepository;
import org.example.backendai.repository.MessageRepository;
import org.example.backendai.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageService {

    UserRepository userRepository;
    MessageMapper messageMapper;
    MessageRepository messageRepository;
    MessageClassificationRepository classificationRepository;

    public MessageResponse sendMessage(MessageRequest request) {
        log.info("Sending message from {} to {} in room {}",
                request.getSenderUsername(), request.getReceiverUsername(), request.getRoomId());

        // Tìm sender
        User sender = userRepository.findByUsername(request.getSenderUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Tìm receiver (nullable cho group chat)
        User receiver = null;
        if (request.getReceiverUsername() != null) {
            receiver = userRepository.findByUsername(request.getReceiverUsername())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        }

        // Tạo message
        Message message = messageMapper.toEntity(request, sender, receiver);
        Message savedMessage = messageRepository.save(message);

        log.info("Message saved with ID: {}", savedMessage.getId());
        return messageMapper.toResponse(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesByRoom(String roomId) {
        log.info("Fetching messages for room: {}", roomId);

        return messageRepository.findByRoomIdOrderBySentAtAsc(roomId)
                .stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesBetweenUsers(String username1, String username2) {
        log.info("Fetching messages between {} and {}", username1, username2);

        User user1 = userRepository.findByUsername(username1)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User user2 = userRepository.findByUsername(username2)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return messageRepository.findMessagesBetweenUsers(user1, user2)
                .stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getUnreadMessages(String username) {
        log.info("Fetching unread messages for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return messageRepository.findByReceiverAndIsReadFalseOrderBySentAtDesc(user)
                .stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesBySender(String username) {
        log.info("Fetching messages from sender: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return messageRepository.findBySenderOrderBySentAtDesc(user)
                .stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getUserRooms(String username) {
        log.info("Fetching rooms for user: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return messageRepository.findRoomsByUser(user);
    }

    public void markAsRead(Integer messageId) {
        log.info("Marking message {} as read", messageId);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        message.setIsRead(true);
        messageRepository.save(message);
    }

    public void markAllAsRead(String username) {
        log.info("Marking all messages as read for user: {}", username);

        // 1. Tìm bác sĩ
        User userDoctor = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        log.info("Doctor found: {}", userDoctor.getUsername());

        // 2. Lấy tất cả tin nhắn chưa đọc mà bác sĩ là người nhận
        List<Message> unreadMessages = messageRepository
                .findByReceiverAndIsReadFalseOrderBySentAtDesc(userDoctor);

        if (unreadMessages.isEmpty()) {
            log.info("No unread messages for doctor: {}", username);
            return;
        }

        log.info("Found {} unread messages", unreadMessages.size());

        // 3. Lấy danh sách bệnh nhân đã gửi tin nhắn (loại bỏ trùng lặp)
        List<User> patients = unreadMessages.stream()
                .map(Message::getSender)
                .distinct()
                .toList();

        log.info("Found {} unique patients who sent messages", patients.size());

        // 4. Xóa classification của từng bệnh nhân
        for (User patient : patients) {
            try {
                Optional<MessageClassification> classificationOpt =
                        classificationRepository.findByPatientId(patient.getId());

                if (classificationOpt.isPresent()) {
                    classificationRepository.delete(classificationOpt.get());
                    log.info("Deleted classification for patient ID: {}", patient.getId());
                } else {
                    log.warn("No classification found for patient ID: {}", patient.getId());
                }
            } catch (Exception e) {
                log.error("Error deleting classification for patient ID: {}", patient.getId(), e);
            }
        }

        // 5. Đánh dấu tất cả tin nhắn là đã đọc
        unreadMessages.forEach(msg -> msg.setIsRead(true));
        messageRepository.saveAll(unreadMessages);

        log.info("Marked {} messages as read for doctor: {}", unreadMessages.size(), username);
    }


    public void deleteMessage(Integer messageId) {
        log.info("Deleting message: {}", messageId);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        messageRepository.delete(message);
    }

    public void deleteRoomMessages(String roomId) {
        log.info("Deleting all messages from room: {}", roomId);
        messageRepository.deleteByRoomId(roomId);
    }

    @Transactional(readOnly = true)
    public Long countUnreadMessages(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return messageRepository.countByReceiverAndIsReadFalse(user);
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> getMessagesByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Fetching messages between {} and {}", start, end);

        return messageRepository.findBySentAtBetweenOrderBySentAtDesc(start, end)
                .stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }
}