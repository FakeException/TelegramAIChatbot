package me.darkboy.response;

import lombok.Getter;
import lombok.Setter;
import me.darkboy.message.ChatMessage;

@Getter
@Setter
public class Choice {
    private int index;
    private String finish_reason;
    private ChatMessage message;

    @Override
    public String toString() {
        return "Choice{" +
                "index=" + index +
                ", finish_reason='" + finish_reason + '\'' +
                ", message=" + message +
                '}';
    }
}
