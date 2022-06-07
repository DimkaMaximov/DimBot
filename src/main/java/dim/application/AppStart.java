package dim.application;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendAudio;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.IOException;

public class AppStart {
    public static void main(String[] args) {
        System.out.println("");

        TelegramBot bot = new TelegramBot("5458274042:AAEGwf-GqjxUAP6k39NOt46jwjd3JTkE5ls");

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {

                Keyboard keyboard = new ReplyKeyboardMarkup("Вычислить петуха", "Вычислить красавчика");

                if (update.message().text().equals("Hi")) {
                    bot.execute(new SendAudio(update.message().chat().id(), "Выбери опцию")
                            .replyMarkup(keyboard));
                }

                if (update.message().text().equals("/start")) {
                    SendMessage message = new SendMessage(update.message().chat().id(), "Hello. This is start message");
                    bot.execute(message);
                }

                if (update.message().text().equals("Hi")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Ну привет"));

                } else if (update.message().text().equals("Ты даун")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Ты отвечаешь за свои слова?")
                            .replyToMessageId(update.message().messageId()));

                } else if (update.message().text().equals("Да")) {
                    bot.execute(new SendMessage(update.message().chat().id(), "Тоби пизда, ходи оглядывайся"));

                } else {
                    bot.execute(new SendMessage(update.message().chat().id(), "Ну привет"));
                }

                bot.execute(new SendMessage(update.message().chat().id(), ""),

                        //ответить на сообщение
//                        bot.execute(new SendMessage(update.message().chat().id(), "Приветик")
//                                        .replyToMessageId(update.message().messageId()),

                        new Callback<SendMessage, SendResponse>() {
                            @Override
                            public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
                                System.out.println(sendResponse.toString());
                            }

                            @Override
                            public void onFailure(SendMessage sendMessage, IOException e) {
                            }
                        });

                //добавить клавиатуру
//                bot.execute(
//                        new SendAudio(update.message().chat().id(),
//                                new File("src/main/resources/audio/otryshka.mp3"))
//                                .replyMarkup(keyboard));
            });

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }
}