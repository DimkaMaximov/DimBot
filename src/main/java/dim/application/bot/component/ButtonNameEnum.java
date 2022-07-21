package dim.application.bot.component;

/**
 * Названия кнопок основной клавиатуры
 */
public enum ButtonNameEnum {
    IDENTIFY_ROOSTER("Крути петушиный барабан"),
    SHOW_STATISTIC("Покажи ПетушСтат"),
    DO_SOMETHING("Сделай что-нибудь"),
    SEND_ME_A_CAT("Пришли мне котика"),
    ESTIMATE_AVATAR("Оцени мою аватарку"),
    SAY_ME_A_COMPLIMENT("Скажи мне что-нибудь приятное");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
