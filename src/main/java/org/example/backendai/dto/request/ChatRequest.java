package org.example.backendai.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private String attachmentUrl;
}