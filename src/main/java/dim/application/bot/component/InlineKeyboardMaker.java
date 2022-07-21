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
                ButtonNameEnum.IDENTIFY_ROOSTER.getButtonName(), ButtonNameEnum.IDENTIFY_ROOSTER.getButtonName(),
                ButtonNameEnum.SHOW_STATISTIC.getButtonName(), ButtonNameEnum.SHOW_STATISTIC.getButtonName()));

        rowList.add(getTwoButtonsInRow(
                ButtonNameEnum.DO_SOMETHING.getButtonName(), ButtonNameEnum.DO_SOMETHING.getButtonName(),
                ButtonNameEnum.SEND_ME_A_CAT.getButtonName(), ButtonNameEnum.SEND_ME_A_CAT.getButtonName()));

        rowList.add(getButtonInRow(ButtonNameEnum.SAY_ME_A_COMPLIMENT.getButtonName(), ButtonNameEnum.SAY_ME_A_COMPLIMENT.getButtonName()));

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