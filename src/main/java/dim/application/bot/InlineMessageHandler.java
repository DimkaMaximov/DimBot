package dim.application.bot;

import dim.application.bot.component.Compliment;
import dim.application.bot.component.Divination;
import dim.application.bot.component.InlineKeyboardMaker;
import dim.application.bot.component.ReplyKeyboardMaker;
import dim.application.bot.component.Stickers;
import dim.application.model.entity.Rooster;
import dim.application.service.api.RoosterService;
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
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class InlineMessageHandler {

    @Autowired
    private RoboCatBot bot;

    @Autowired
    private RoosterService service;

    @Autowired
    private ReplyKeyboardMaker replyKeyboardMaker;

    @Autowired
    private InlineKeyboardMaker inlineKeyboardMaker;

    private Random randomGen = new Random();

    public String handleMessage(Update update) {

        String chatId;
        String message;
        String userName;

        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            message = update.getCallbackQuery().getData();
            userName = update.getCallbackQuery().getFrom().getFirstName();
        } else {
            chatId = update.getMessage().getChatId().toString();
            message = update.getMessage().getText();
            userName = update.getMessage().getFrom().getFirstName();
        }

        if (Utils.checkBadWords(bot, message, chatId, update)) {
            return "";
        }

        switch (message) {
            case "/start":
            case "/start@robo_cat_bot":
                SendMessage badMessage2 = new SendMessage(chatId, userName + ", что будем делать?");
                badMessage2.enableMarkdown(true);
                badMessage2.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtons());
                bot.sendMessage(badMessage2);
                return "";

            case "Крути петушиный барабан": {
                if (bot.getDateForRooster() == null || !bot.getDateForRooster().isEqual(LocalDate.now())) {
                    bot.setDateForRooster(LocalDate.now());
                } else {
                    SendMessage sm = new SendMessage(chatId,
                            userName + ", уже все знают, кто сегодня петух \uD83D\uDE09");
                    bot.sendMessage(sm);
                    return "";
                }
                if (LocalDate.now().getDayOfMonth() == 1) {
                    service.clearStatistics();
                }

                Utils.checkUsers(service, update);

                List<Rooster> roosters = service.getAll();

                bot.sendMessage(new SendMessage(chatId, "Кручу петушиный барабан ⏱\uD83D\uDC14"));

                estimationResponse(chatId);

                Collections.shuffle(roosters);

                Rooster newRooster = roosters.get(0);
                newRooster.setMonthCount(newRooster.getMonthCount() + 1);
                service.save(newRooster);

                return roosters.isEmpty() ? "\uD83C\uDF89 Сегодня все петухи" : "\uD83C\uDF89 Сегодня петух - " + newRooster.getFullName();
            }

            case "Покажи ПетушСтат":
                StringBuilder stringBuilder = new StringBuilder();

                service.getAll().stream()
                        .sorted(Comparator.comparingLong(Rooster::getMonthCount).reversed())
                        .collect(Collectors.toList())
                        .forEach(rooster -> stringBuilder.append(rooster.getFullName()).append(" = ").append(rooster.getMonthCount()).append("\n"));

                bot.sendMessage(new SendMessage(chatId, userName + ", вот тебе петушиная статистика за месяц:\n\n" + stringBuilder));
                return "";

            case "Сделай что-нибудь":
                //SendAudio audioMessage = new SendAudio(chatId, new InputFile(new File("src/main/resources/audio/audio_2.mp3")));
                SendAudio audioMessage = new SendAudio(chatId, new InputFile(new File("target/classes/audio/audio_2.mp3")));
                try {
                    bot.execute(audioMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return "";

            case "Скажи мне что-нибудь приятное":
                List<String> compliments = new ArrayList<>();
                compliments.addAll(0, Compliment.compliments);
                Collections.shuffle(compliments);
                bot.sendMessage(new SendMessage(chatId, userName + ", " + compliments.get(0)));
                return "";

            //case "С днем рождения":

            case "Пришли мне котика":
                bot.sendMessage(new SendMessage(chatId, userName + ", вот тебе котик \uD83D\uDC08:"));
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

        int random = randomGen.nextInt(2) + 3;
        Collections.shuffle(list);

        while (random >= 0) {
            SendMessage message = new SendMessage(chatId, list.get(random));
            bot.sendMessage(message);
            list.remove(random);
            random--;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
