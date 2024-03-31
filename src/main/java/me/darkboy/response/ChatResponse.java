package me.darkboy.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatResponse {

    private String model;
    private long created;
    private List<Choice> choices;

    @Override
    public String toString() {
        return "ChatResponse{" +
                "model='" + model + '\'' +
                ", created=" + created +
                ", choices=" + choices +
                '}';
    }
}
