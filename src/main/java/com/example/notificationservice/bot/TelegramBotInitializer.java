package com.example.notificationservice.bot;

import com.example.notificationservice.Service.impl.TelegramBotService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBotInitializer {

    @Autowired
    TelegramBotService bot;

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
}