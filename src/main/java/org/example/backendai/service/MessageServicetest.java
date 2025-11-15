package org.example.backendai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Log4j2
@Service
public class MessageServicetest extends TextWebSocketHandler {

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyAjLzryQH7POkFP7NvA84KCYpbeIB3dp9M";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userMessage = message.getPayload();
        log.info("Client message: {}", userMessage);

        try {
            // Gọi Gemini API
            String aiReply = callGeminiAPI(userMessage);

            // Gửi lại kết quả cho client
            session.sendMessage(new TextMessage(aiReply));

        } catch (Exception e) {
            log.error("Error calling Gemini API", e);
            session.sendMessage(new TextMessage("⚠️ Lỗi khi gọi Gemini API: " + e.getMessage()));
        }
    }

    private String callGeminiAPI(String userPrompt) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Body gửi lên Gemini
        String body = """
        {
          "contents": [
            {
              "parts": [
                { "text": "%s" }
              ]
            }
          ]
        }
        """.formatted(userPrompt);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =
                restTemplate.exchange(GEMINI_API_URL, HttpMethod.POST, request, String.class);

        // Parse kết quả trả về
        JsonNode json = mapper.readTree(response.getBody());
        JsonNode textNode = json.path("candidates").get(0).path("content").path("parts").get(0).path("text");

        return textNode.asText("Không nhận được phản hồi từ Gemini.");
    }
}
