package dim.application.bot.component;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardMaker {

    public InlineKeyboardMarkup getInlineMessageButtons() {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(getTwoButtonsInRow(
                "Крути петушиный барабан", "Крути петушиный барабан",
                "Выбери красавчика", "Выбери красавчика"));

        rowList.add(getTwoButtonsInRow(
                "Сделай что-нибудь", "Сделай что-нибудь",
                "Пришли мне котика", "Пришли мне котика"));

        rowList.add(getButtonInRow("Скажи мне что-нибудь приятное", "Скажи мне что-нибудь приятное"));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getButtonInRow(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }

    private List<InlineKeyboardButton> getTwoButtonsInRow(String firstButtonName, String firstButtonCallBackData,
                                                          String secondButtonName, String secondButtonCallBackData) {
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(firstButtonName);
        button1.setCallbackData(firstButtonCallBackData);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(secondButtonName);
        button2.setCallbackData(secondButtonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button1);
        keyboardButtonsRow.add(button2);
        return keyboardButtonsRow;
    }
}