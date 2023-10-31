package com.example.notification;

import com.example.notification.bot.config.TelegramBotConfig;
import com.example.notification.bot.TelegramBotService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@SpringBootTest
public class BaseTest {

    @MockBean
    public TelegramBotService mocktelegramBotService;

    @MockBean
    public TelegramBotsApi telegramBotsApi;

    @MockBean
    private TelegramBotConfig telegramBotConfig;
}