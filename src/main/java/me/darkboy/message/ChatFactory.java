package me.darkboy.message;

import me.darkboy.requests.ChatRequest;
import me.darkboy.enums.Role;

public class ChatFactory {

    public void addChatMessage(ChatRequest chatRequest, Role role, String message) {
        chatRequest.getMessages().add(new ChatMessage(role.name().toLowerCase(), message));
    }
}
