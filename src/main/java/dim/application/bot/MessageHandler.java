package dim.application.bot;

import dim.application.bot.component.InlineKeyboardMaker;
import dim.application.bot.component.ReplyKeyboardMaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
@Component
public class MessageHandler {

    @Autowired
    private DimTelegramBot bot;

    @Autowired
    private ReplyKeyboardMaker replyKeyboardMaker;

    @Autowired
    private InlineKeyboardMaker inlineKeyboardMaker;

    private Properties properties = new Properties();

    //private List<String> users;

    public String handleMessage(Update update) {

        String chatId;
        String message;
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            message = update.getCallbackQuery().getData();
        } else {
            chatId = update.getMessage().getChatId().toString();
            message = update.getMessage().getText();
        }

        if (checkBadWords(message, chatId, update)) {
            return "";
        }

        switch (message) {
            case "/start":
                SendMessage badMessage = new SendMessage(chatId, "Что будем делать?");
                badMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
                bot.sendMessage(badMessage);
                return "";

            case "Вычислить петуха": {
                checkProperties(update);
                int random = (int) (Math.random() * properties.size());
                List<String> list = new ArrayList<>();
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    list.add(entry.getValue().toString());
                }
                SendMessage goodMessage = new SendMessage(chatId, "Процесс запущен ⏱ \uD83D\uDC14");
                bot.sendMessage(goodMessage);

                estimationResponse(chatId);

                String rooster = list.isEmpty() ? "\uD83C\uDF89 Сегодня все петухи" : "\uD83C\uDF89 Сегодня петух - " + list.get(random);
                SendMessage sm = new SendMessage(chatId, rooster);
                sm.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtons());
                bot.sendMessage(sm);
                return "";
            }

            case "Вычислить красавчика":
                checkProperties(update);
                int random = (int) (Math.random() * properties.size());
                List<String> list = new ArrayList<>();
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    list.add(entry.getValue().toString());
                }
                SendMessage goodMessage = new SendMessage(chatId, "Процесс запущен ⏱");
                bot.sendMessage(goodMessage);
                estimationResponse(chatId);
                return list.isEmpty() ? "Сегодня все красавчики" : "\uD83C\uDF89 Сегодня красавчик - " + list.get(random);

            case "Сделай что-нибудь":
                SendAudio audioMessage = new SendAudio(chatId, new InputFile(new File("src/main/resources/audio/otryshka.mp3")));
                try {
                    bot.execute(audioMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return "";

            //case "С днем рождения":

            case "Пришли мне котика":
                List<String> catsList = new ArrayList<>();
                catsList.addAll(0, Utils.stickersList);
                Collections.shuffle(catsList);
                SendSticker stickerMessage = new SendSticker(chatId, new InputFile(catsList.get(0)));
                try {
                    bot.execute(stickerMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return "";

            case "Согласиться":
                int newRun = (int) ((Math.random() * 10));
                return newRun > 5 ? "Я тоже с этим согласен" : "Мне кажется что сегодня петух кто-то другой...";

            case "Крути барабан заново":
                checkProperties(update);
                int newRandom = (int) (Math.random() * properties.size());
                List<String> newList = new ArrayList<>();
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    newList.add(entry.getValue().toString());
                }
                SendMessage newGoodMessage = new SendMessage(chatId, "Процесс запущен ⏱ \uD83D\uDC14");
                bot.sendMessage(newGoodMessage);

                estimationResponse(chatId);

                String rooster = newList.isEmpty() ? "\uD83C\uDF89 Сегодня все петухи" : "\uD83C\uDF89 Сегодня петух - " + newList.get(newRandom);
                SendMessage sm = new SendMessage(chatId, rooster);
                sm.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtons());
                bot.sendMessage(sm);
                return "";

            case "/stop":
                return "Всем пока! Хорошего дня, котики!";

            default:
                return "Ты глупый? Команду правильно сформулируй";
        }
    }

    public void estimationResponse(String chatId) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        List<String> list = new ArrayList<>();
        list.addAll(0, Utils.divinationList);

        int random = (int) (1 + (Math.random()) * 5);
        int count = 1;

        while (count < random) {
            Collections.shuffle(list);
            SendMessage message = new SendMessage(chatId, list.get(0));
            bot.sendMessage(message);
            list.remove(0);
            count++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void checkProperties(Update update) {
        try {
            properties.load(new FileReader("src/main/resources/users/users.properties"));
            User user = null;
            if (update.hasMessage()) {
                user = update.getMessage().getFrom();
            } else if (update.hasCallbackQuery()) {
                user = update.getCallbackQuery().getMessage().getFrom();
            }
            if (!properties.contains(user)) {
                String firstName = user.getFirstName() != null ? user.getFirstName() : "";
                String lastName = user.getLastName() != null ? user.getLastName() : "";
                properties.setProperty(user.getUserName(), firstName + " " + lastName);
                properties.store(new FileOutputStream("src/main/resources/users/users.properties"), null);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkBadWords(String message, String chatId, Update update) {
        boolean check = false;
        for (String badWord : BadWords.badWords) {
            if (message.toLowerCase().contains(badWord)) {
                SendMessage sm = new SendMessage(chatId,
                        update.getMessage().getChat().getFirstName() + ", не ругайся! Напиши вежливо");
                sm.setReplyToMessageId(update.getMessage().getMessageId());
                bot.sendMessage(sm);
                check = true;
                break;
            }
        }
        return check;
    }
}
