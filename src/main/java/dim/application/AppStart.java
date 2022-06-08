package dim.application;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import dim.application.bot.DimBot;
import dim.application.bot.MessageHandler;

import java.io.IOException;

public class AppStart {
    public static void main(String[] args) {

        TelegramBot bot = new DimBot();
        MessageHandler messageHandler = new MessageHandler(bot);

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {

                if (update.message() != null && update.message().text() != null) {
                    String message = messageHandler.handleMessage(update);

                    bot.execute(new SendMessage(update.message().chat().id(), message),

                            new Callback<SendMessage, SendResponse>() {
                                @Override
                                public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
                                    System.out.println(sendResponse.toString());
                                }

                                @Override
                                public void onFailure(SendMessage sendMessage, IOException e) {
                                    System.out.println(sendMessage.toString());
                                }
                            });
                }
            });

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}