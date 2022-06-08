package dim.application.bot;

import com.pengrad.telegrambot.TelegramBot;

public class DimBot extends TelegramBot {

    private final static String token = "5458274042:AAEGwf-GqjxUAP6k39NOt46jwjd3JTkE5ls";

    public DimBot() {
        super(token);
    }
}