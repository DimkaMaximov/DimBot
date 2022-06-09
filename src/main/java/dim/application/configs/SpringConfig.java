package dim.application.configs;

import dim.application.bot.DimTelegramBot;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class SpringConfig {

    //private final TelegramConfig telegramConfig;

    @Bean
    public DimTelegramBot dimTelegramBot() {
        return new DimTelegramBot();
    }
}
