package com.smartgate.main;

import okhttp3.*;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class SmsApi {

    private final OkHttpClient client;
    private final String apiUrl = "https://ggqxve.api.infobip.com/sms/2/text/advanced";
    private final String apiKey = "App 76bb67f2a20292b08c108cbdb93fca5e-604a33ab-3327-4967-b548-aebb0f01b907";

    public SmsApi(OkHttpClient client) {
        this.client = client;
    }

    public String sendSms(String phoneNumber) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = String.format("{\"messages\":[{\"destinations\":[{\"to\":\"%s\"}],\"from\":\"Phone Code\",\"text\":\"ah ti ok\"}]}", phoneNumber);

        RequestBody body = RequestBody.create(jsonBody, mediaType); // Updated method
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", apiKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
