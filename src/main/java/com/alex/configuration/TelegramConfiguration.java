package com.alex.configuration;

import com.alex.telegram.TelegramNotifierBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.generics.BotSession;
import org.telegram.telegrambots.generics.Webhook;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

@Slf4j
@Configuration
public class TelegramConfiguration {
    @Value("${telegram.bot.token}")
    private String botToken;

//    @Bean
//    public TelegramNotifierBot telegramNotifierBot() {
//        ApiContext.register(BotSession.class, DefaultBotSession.class);
//        ApiContext.register(Webhook.class, DefaultWebhook.class);
//
//        TelegramNotifierBot bot = new TelegramNotifierBot();
//        bot.setBotToken(botToken);
//        TelegramBotsApi botsApi = new TelegramBotsApi();
//        try {
//            botsApi.registerBot(bot);
//        } catch (TelegramApiException e) {
//            log.error("Can't start telegram bot", e);
//        }
//        return bot;
//    }
}
