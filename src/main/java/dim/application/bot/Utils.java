package dim.application.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class Utils {

    public static void checkProperties(Properties properties, Update update) {
        try {
            properties.load(new FileReader("src/main/resources/users/users.properties"));
            //properties.load(new FileReader(System.getProperty("user.home") + File.separator + "users.properties"));
            User user = null;
            if (update.hasMessage()) {
                user = update.getMessage().getFrom();
            } else if (update.hasCallbackQuery()) {
                user = update.getCallbackQuery().getMessage().getFrom();
            }
            if (!properties.contains(user) && !user.getUserName().equals("robo_cat_bot")) {
                String firstName = user.getFirstName() != null ? user.getFirstName() : "";
                String lastName = user.getLastName() != null ? user.getLastName() : "";
                properties.setProperty(user.getUserName(), firstName + " " + lastName);
                properties.store(new FileOutputStream("src/main/resources/users/users.properties"), null);
                //properties.store(new FileOutputStream("System.getProperty("user.home") + File.separator + "users.properties""), null);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
