package org.example.backendai;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AIClient {

    public static void main(String[] args) {

        // ✅ 1. Khai báo đường dẫn ảnh và URL API
        String url = "https://highlight-kidney-edt-pattern.trycloudflare.com/analyze";
        String imagePath =  "D:\\Workspace\\AIForLife\\SieuNhanAI\\ho so benh an.jpg";

        // ✅ 2. Kiểm tra file tồn tại
        File file = new File(imagePath);
        if (!file.exists() || file.length() == 0) {
            System.err.println("❌ Lỗi: Ảnh không tồn tại hoặc rỗng!");
            return;
        }

        System.out.println("📸 Gửi file: " + file.getName() + " (" + file.length() + " bytes)");

        // ✅ 3. Cấu hình HTTP client với timeout phù hợp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        // ✅ 4. Tạo RequestBody multipart/form-data
        RequestBody fileBody = RequestBody.create(file, MediaType.parse("image/jpeg"));
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        // ✅ 5. Tạo request kèm header
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();

        // ✅ 6. Gửi request và đọc response
        try (Response response = client.newCall(request).execute()) {
            System.out.println("📡 Response code: " + response.code());
            System.out.println("📜 Headers: " + response.headers());

            if (!response.isSuccessful()) {
                System.err.println("❌ Lỗi khi gọi API: " + response.code() + " - " + response.message());

                // In chi tiết lỗi từ server
                try (ResponseBody errorBody = response.body()) {
                    if (errorBody != null) {
                        System.err.println("Chi tiết lỗi: " + errorBody.string());
                    }
                }
                return;
            }

            String jsonResponse = null;
            try (ResponseBody responseBody = response.body()) {
                if (responseBody == null) {
                    System.err.println("⚠️ Response body rỗng.");
                    return;
                }
                jsonResponse = responseBody.string();
            }

            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                System.err.println("⚠️ Không có dữ liệu trả về từ server.");
                return;
            }

            System.out.println("✅ Kết quả JSON:");
            System.out.println(jsonResponse);

            // ✅ 7. Parse JSON và in đẹp hơn (optional)
            try {
                ObjectMapper mapper = new ObjectMapper();
                Object json = mapper.readValue(jsonResponse, Object.class);
                // Thêm vào code

                String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                System.out.println("\n📋 JSON đã format:");
                System.out.println(prettyJson);
            } catch (Exception e) {
                System.out.println("⚠️ Không thể format JSON: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("❌ Lỗi kết nối API:");
            e.printStackTrace();
        }
    }
}