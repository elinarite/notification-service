package com.example.notificationservice.bot;

import com.example.notificationservice.Service.impl.TelegramBotService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
//@Configuration
public class TelegramBotInitializer {

    @Autowired
    TelegramBotService bot;

//    @EventListener({ContextRefreshedEvent.class})
//    public void init() throws TelegramApiException {
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        try {
//            telegramBotsApi.registerBot(bot);
//        }
//        catch (TelegramApiException e) {
////            log.error("Error occurred: " + e.getMessage());
//        }
//    }

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBotService telegramBotService) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(telegramBotService);
        return api;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

//    @Bean
//    public StandardWebSocketClient standardWebSocketClient() {
//        return new StandardWebSocketClient();
//    }
}