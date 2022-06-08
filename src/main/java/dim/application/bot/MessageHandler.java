package dim.application.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendAudio;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageHandler {

    private DimBot bot;

    private ReplyKeyboardMarkup keyboard;

    private Properties properties = new Properties();

    private List<String> users;

    public <T extends TelegramBot> MessageHandler(T bot) {
        this.bot = (DimBot) bot;
        keyboard = new ReplyKeyboardMarkup("Вычислить петуха", "Вычислить красавчика", "Сделай что-нибудь", "Пришли мне котика");
        keyboard.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public String handleMessage(Update update) {

        Object chatId = update.message().chat().id();
        String message = update.message().text();

        for (String badWord : BadWords.badWords) {
            if (message.contains(badWord)) {
                bot.execute(new SendMessage(chatId,
                        update.message().chat().firstName() + ", не ругайся! Напиши вежливо").replyToMessageId(update.message().messageId()));
                return "";
            }
        }

        switch (message) {
            case "/start":
                bot.execute(new SendMessage(chatId, "Что будем делать?")
                        .replyMarkup(keyboard));
                return "";

            case "Вычислить петуха": {
                checkProperties(update);
                int random = (int) (Math.random() * properties.size());
                List<String> list = new ArrayList<>();
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    list.add(entry.getValue().toString());
                }
                bot.execute(new SendMessage(chatId, "Процесс запущен ⏱ \uD83D\uDC14"));
                estimationResponse(update);
                return list.isEmpty() ? "\uD83C\uDF89 Сегодня все петухи" : "\uD83C\uDF89 Сегодня петух - " + list.get(random);
            }

            case "Вычислить красавчика":
                checkProperties(update);
                int random = (int) (Math.random() * properties.size());
                List<String> list = new ArrayList<>();
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    list.add(entry.getValue().toString());
                }
                bot.execute(new SendMessage(chatId, "Процесс запущен ⏱"));
                estimationResponse(update);
                return list.isEmpty() ? "Сегодня все красавчики" : "\uD83C\uDF89 Сегодня красавчик - " + list.get(random);

            case "Сделай что-нибудь":
                bot.execute(new SendAudio(chatId, new File("src/main/resources/audio/otryshka.mp3")));
                return "";

            //case "С днем рождения":
            case "Пришли мне котика":
                List<String> catsList = new ArrayList<>();
                catsList.addAll(0, Utils.stickersList);
                Collections.shuffle(catsList);
                bot.execute(new SendSticker(chatId, catsList.get(0)));
                return "";

            case "/end":
                return "Всем пока! Хорошего дня, котики!";

            default:
                return "Ты глупый? Команду правильно сформулируй";
        }
    }

    public void estimationResponse(Update update) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        List<String> list = new ArrayList<>();
        list.addAll(0, Utils.divinationList);

        int random = (int) (1 + (Math.random())*5);
        int count = 1;

        while (count < random) {
            Collections.shuffle(list);
            bot.execute(new SendMessage(update.message().chat().id(), list.get(0)));
            list.remove(0);
            count++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void checkProperties(Update update){
        try {
            properties.load(new FileReader("src/main/resources/users/users.properties"));
            User user = update.message().from();
            if (!properties.contains(user)) {
                String firstName = user.firstName() != null ? user.firstName() : "";
                String lastName = user.lastName() != null ? user.lastName() : "";
                properties.setProperty(user.username(), firstName + " " + lastName);
                properties.store(new FileOutputStream("src/main/resources/users/users.properties"), null);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
