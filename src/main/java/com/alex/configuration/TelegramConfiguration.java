package com.alex.configuration;

//@Slf4j
//@Configuration
//public class TelegramConfiguration {
//    @Value("${telegram.bot.token}")
//    private String botToken;
//
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
//}