package dim.application.bot.component;

/**
 * Названия кнопок основной клавиатуры
 */
public enum ButtonNameEnum {
    IDENTIFY_ROOSTER("Крути петушиный барабан"),
    IDENTIFY_PRETTY("Вычислить красавчика"),
    DO_SOMETHING("Сделай что-нибудь"),
    SEND_ME_CAT("Пришли мне котика");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
