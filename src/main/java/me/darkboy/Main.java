package me.darkboy;

import me.darkboy.api.NewAPI;
import me.darkboy.enums.Role;
import me.darkboy.message.ChatFactory;
import me.darkboy.message.ChatMessage;
import me.darkboy.requests.ChatRequest;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends TelegramLongPollingBot {

    private static HashMap<Long, ChatRequest> messages;
    private final ExecutorService service = Executors.newSingleThreadExecutor();

    private static ChatFactory chatFactory;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {

            if (update.getMessage().hasText()) {
                // Get the chat ID and message text from the incoming update
                long chatId = update.getMessage().getChatId();
                String messageText = update.getMessage().getText();

                switch (messageText) {
                    case "/remove" -> {
                        SendMessage message = new SendMessage();
                        message.setChatId(chatId);

                        if (messages.containsKey(chatId)) {
                            ChatRequest chatRequest = messages.get(chatId);
                            List<ChatMessage> messagesList = chatRequest.getMessages();
                            ChatMessage lastMessage  = messagesList.get(messagesList.size() - 1);
                            messagesList.remove(lastMessage);
                            message.setText("Last message deleted.");
                        } else {
                            message.setText("Failed to delete the message.");
                        }

                        try {

                            execute(message);

                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                            // Handle exceptions
                        }
                    }
                    case "/regenerate" -> {

                        if (messages.containsKey(chatId)) {
                            ChatRequest chatRequest = messages.get(chatId);
                            List<ChatMessage> messagesList = chatRequest.getMessages();
                            ChatMessage lastMessage  = messagesList.get(messagesList.size() - 1);
                            messagesList.remove(lastMessage);
                            sendRequest(chatId);
                        }
                    }
                    case "/reset" -> {
                        messages.remove(chatId);
                        SendMessage message = new SendMessage();
                        message.setChatId(chatId);
                        message.setText("Chat resetted.");
                        try {
                            // Send the message
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    case "." -> sendRequest(chatId);
                    default -> {
                        if (!messages.containsKey(chatId)) {
                            ChatRequest chatRequest = new ChatRequest();
                            chatRequest.setMode("chat");
                            chatRequest.setCharacter("Misaki");
                            chatFactory.addChatMessage(chatRequest, Role.USER, messageText);
                            messages.put(chatId, chatRequest);
                        } else {
                            chatFactory.addChatMessage(messages.get(chatId), Role.USER, messageText);
                        }

                        sendRequest(chatId);
                    }
                }
            }
        }
    }

    private void sendRequest(long chatId) {
        // Create a response message
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        service.execute(() -> {

            ChatMessage chatMessage = NewAPI.answer(messages.get(chatId));

            message.setText(chatMessage.getContent());

            System.out.println(messages.get(chatId));

            try {
                // Send the message
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String getBotUsername() {
        // Return your bot's username
        return "misaki_bot";
    }

    @Override
    public String getBotToken() {
        // Return your bot's token
        return "";
    }

    public static void main(String[] args) {

        chatFactory = new ChatFactory();
        messages = new HashMap<>();

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Main());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
