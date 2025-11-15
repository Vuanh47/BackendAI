package org.example.backendai.mapper;

import lombok.RequiredArgsConstructor;
import org.example.backendai.dto.request.MessageRequest;
import org.example.backendai.dto.response.MessageResponse;
import org.example.backendai.entity.Message;
import org.example.backendai.entity.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    public Message toEntity(MessageRequest request, User sender, User receiver) {
        return Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .type(request.getType())
                .roomId(request.getRoomId())
                .attachmentURL(request.getAttachmentURL())
                .isRead(false)
                .build();
    }

    public MessageResponse toResponse(Message entity) {
        return MessageResponse.builder()
                .messageID(entity.getMessageID())
                .senderUsername(entity.getSender() != null ? entity.getSender().getUsername() : null)
                .receiverUsername(entity.getReceiver() != null ? entity.getReceiver().getUsername() : null)
                .content(entity.getContent())
                .type(entity.getType())
                .sentAt(entity.getSentAt())
                .roomId(entity.getRoomId())
                .isRead(entity.getIsRead())
                .attachmentURL(entity.getAttachmentURL())
                .success(true)
                .build();
    }
}
