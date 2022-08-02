package dim.application.bot;

import dim.application.model.entity.Rooster;
import dim.application.service.api.RoosterService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static void checkProperties(RoosterService service, Update update) {
        List<Rooster> list = service.getAll();
        List<String> users = new ArrayList<>();
        for (Rooster rooster : list) {
            users.add(rooster.getLogin());
        }
        User user = null;
        if (update.hasMessage()) {
            user = update.getMessage().getFrom();
        } else if (update.hasCallbackQuery()) {
            user = update.getCallbackQuery().getMessage().getFrom();
        }
        if (!users.contains(user.getUserName()) && !user.getUserName().equals("robo_cat_bot")) {
            String firstName = user.getFirstName();
            String lastName = user.getLastName() != null ? user.getLastName() : "";
            service.save(new Rooster(user.getUserName(), firstName + " " + lastName));
        }
    }

    public static boolean checkBadWords(RoboCatBot bot, String message, String chatId, Update update) {
        boolean check = Arrays.stream(BadWords.badWords).anyMatch(message.toLowerCase()::contains);
        if (check) {
            SendMessage sm = new SendMessage(chatId,
                    update.getMessage().getFrom().getFirstName() + ", не ругайся! Пиши вежливо");
            sm.setReplyToMessageId(update.getMessage().getMessageId());
            bot.sendMessage(sm);
        }
        return check;
    }
}
