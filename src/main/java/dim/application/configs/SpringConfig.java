package dim.application.configs;

import dim.application.bot.RoboCatBot;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class SpringConfig {

    //private final TelegramConfig telegramConfig;

    @Bean
    public RoboCatBot dimTelegramBot() {
        return new RoboCatBot();
    }
}
