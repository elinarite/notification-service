package com.example.notification;

import com.example.notification.service.impl.TelegramBotService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@SpringBootTest
public class BaseTest {

    @MockBean
    public TelegramBotService telegramBotService;

//    @MockBean
//    public TelegramBotsApi telegramBotsApi;
//
//    @BeforeEach
//    public void setupBase() {
//        Mockito.when(telegramBotService.someMethod()).thenReturn(expectedValue);
//    }
}
