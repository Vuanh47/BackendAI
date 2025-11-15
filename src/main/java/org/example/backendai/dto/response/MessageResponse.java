package org.example.backendai.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.backendai.constant.MessageType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    Integer messageID;
    String senderUsername;
    String receiverUsername;
    String content;
    MessageType type;
    LocalDateTime sentAt;
    String roomId;
    Boolean isRead;
    String attachmentURL;
    Boolean success;
}