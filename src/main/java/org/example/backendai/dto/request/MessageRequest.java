package org.example.backendai.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.backendai.constant.MessageType;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageRequest {
    String senderUsername;
    String receiverUsername;  // Null nếu là group chat
    String content;
    MessageType type;
    String roomId;            // Null nếu là private chat
    String attachmentURL;
}