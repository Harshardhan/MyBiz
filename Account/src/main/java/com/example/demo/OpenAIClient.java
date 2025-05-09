package com.example.demo;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OpenAIClient {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final OkHttpClient client = new OkHttpClient();

    public String generateMessage(String userPrompt) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        JSONObject payload = new JSONObject();
        payload.put("model", "gpt-3.5-turbo");
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "user").put("content", userPrompt));
        payload.put("messages", messages);

        RequestBody body = RequestBody.create(payload.toString(), JSON);

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "API Error: " + response.message();
            }

            String responseBody = response.body().string();
            JSONObject responseJson = new JSONObject(responseBody);
            return responseJson
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");
        }
    }
}
