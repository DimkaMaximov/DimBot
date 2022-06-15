package dim.application.bot;

import dim.application.bot.component.Divination;
import dim.application.bot.component.InlineKeyboardMaker;
import dim.application.bot.component.ReplyKeyboardMaker;
import dim.application.bot.component.Stickers;
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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class InlineMessageHandler {

    @Autowired
    private RoboCatBot bot;

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

        if (Utils.checkBadWords(bot, message, chatId, update)) {
            return "";
        }

        switch (message) {
            case "/start":
            case "/start@robo_cat_bot":
                SendMessage badMessage2 = new SendMessage(chatId, "Что будем делать?");
                badMessage2.enableMarkdown(true);
                badMessage2.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtons());
                bot.sendMessage(badMessage2);
                return "";

            case "Крути петушиный барабан": {

                if(bot.getDateForRooster() == null || !bot.getDateForRooster().isEqual(LocalDate.now())) {
                    bot.setDateForRooster(LocalDate.now());
                } else {
                    SendMessage sm = new SendMessage(chatId,
                            update.getCallbackQuery().getFrom().getFirstName() + ", уже все знают, кто сегодня петух \uD83D\uDE09");
                    bot.sendMessage(sm);
                    return "";
                }

                Utils.checkProperties(properties, update);

                Random randomGen = new Random();
                int random = randomGen.nextInt(properties.size());
                List<String> list = new ArrayList<>();
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    list.add(entry.getValue().toString());
                }

                bot.sendMessage(new SendMessage(chatId, "Процесс запущен ⏱ \uD83D\uDC14"));

                estimationResponse(chatId);

                return list.isEmpty() ? "\uD83C\uDF89 Сегодня все петухи" : "\uD83C\uDF89 Сегодня петух - " + list.get(random);
            }

            case "Вычислить красавчика":
                if(bot.getDateForPretty() == null || !bot.getDateForPretty().isEqual(LocalDate.now())) {
                    bot.setDateForPretty(LocalDate.now());
                } else {
                    SendMessage sm = new SendMessage(chatId,
                            update.getCallbackQuery().getFrom().getFirstName() + ", красавчик на сегодня уже известен \uD83D\uDE09");
                    bot.sendMessage(sm);
                    return "";
                }

                Utils.checkProperties(properties, update);

                Random randomGen = new Random();
                int random = randomGen.nextInt(properties.size());
                List<String> list = new ArrayList<>();
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    list.add(entry.getValue().toString());
                }

                bot.sendMessage(new SendMessage(chatId, "Процесс запущен ⏱"));

                estimationResponse(chatId);

                return list.isEmpty() ? "Сегодня все красавчики" : "\uD83C\uDF89 Сегодня красавчик - " + list.get(random);

            case "Сделай что-нибудь":
                SendAudio audioMessage = new SendAudio(chatId, new InputFile(new File("src/main/resources/audio/audio_2.mp3")));
                try {
                    bot.execute(audioMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return "";

            //case "С днем рождения":
            //case "Оцени мою аватарку":

            case "Пришли мне котика":
                List<String> catsList = new ArrayList<>();
                catsList.addAll(0, Stickers.stickersList);
                Collections.shuffle(catsList);
                SendSticker stickerMessage = new SendSticker(chatId, new InputFile(catsList.get(0)));
                try {
                    bot.execute(stickerMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return "";

            case "/stop":
            case "/stop@robo_cat_bot":
                return "Всем пока! Хорошего дня, котики!";

            default:
                return "";
        }
    }

    public void estimationResponse(String chatId) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        List<String> list = new ArrayList<>();
        list.addAll(0, Divination.divinationList);

        Random randomGen = new Random();
        int random = randomGen.nextInt(2)+3;
        int count = 1;

        while (count <= random) {
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
}
