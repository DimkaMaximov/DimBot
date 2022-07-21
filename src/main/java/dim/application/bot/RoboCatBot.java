package dim.application.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

public class RoboCatBot extends TelegramLongPollingBot {

    @Value("${telegram.bot-name}")
    private String username;

    @Value("${telegram.bot-token}")
    private String token;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private InlineMessageHandler inlineMessageHandler;

    @Getter
    @Setter
    private LocalDate dateForRooster;

    @Getter
    @Setter
    private LocalDate dateForPretty;

    @Getter
    @Setter
    private Month month;

    @Getter
    @Setter
    private HashMap<String, Integer> statistic = new HashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String chatId = update.getMessage().getChatId().toString();

            String message = inlineMessageHandler.handleMessage(update);

            if (!message.equals("") || !message.isEmpty()) {
                SendMessage sm = new SendMessage();
                sm.setChatId(chatId);
                sm.setText(message);
                sendMessage(sm);
            }
        } else if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String message = inlineMessageHandler.handleMessage(update);

            if (!message.equals("") || !message.isEmpty()) {
                SendMessage sm = new SendMessage();
                sm.setChatId(chatId);
                sm.setText(message);
                sendMessage(sm);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void sendMessage(SendMessage message) {
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
