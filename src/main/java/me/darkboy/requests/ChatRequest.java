package me.darkboy.requests;

import lombok.Getter;
import lombok.Setter;
import me.darkboy.message.ChatMessage;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatRequest {

    private List<ChatMessage> messages;
    private String mode;
    private String character;

    public ChatRequest() {
        this.messages = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ChatRequest{" +
                "messages=" + messages +
                ", mode='" + mode + '\'' +
                ", character='" + character + '\'' +
                '}';
    }
}
