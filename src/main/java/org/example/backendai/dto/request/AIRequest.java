package org.example.backendai.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class AIRequest {
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
