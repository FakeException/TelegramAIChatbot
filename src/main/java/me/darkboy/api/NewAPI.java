package me.darkboy.api;

import com.google.gson.Gson;
import me.darkboy.message.ChatMessage;
import me.darkboy.requests.ChatRequest;
import me.darkboy.response.ChatResponse;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NewAPI {

    public static ChatMessage answer(ChatRequest chatRequest) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .build();

        Gson gson = new Gson();
        String json = gson.toJson(chatRequest);

        // Define the media type for JSON
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        // Create the request body with the JSON string
        RequestBody body = RequestBody.create(json, mediaType);

        // Build the request
        Request request = new Request.Builder()
                .url("http://127.0.0.1:5000/v1/chat/completions")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            assert response.body() != null;
            String jsonResponse = response.body().string();

            // Deserialize the JSON string into a ChatResponse object
            ChatResponse chatResponse = gson.fromJson(jsonResponse, ChatResponse.class);

            ChatMessage message = chatResponse.getChoices().get(0).getMessage();

            chatRequest.getMessages().add(message);

            return message;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
