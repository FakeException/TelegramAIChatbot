package me.darkboy.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    private String role;
    private String content;

    @Override
    public String toString() {
        return "ChatMessage{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
