package com.example.notification;

import com.example.notification.service.impl.TelegramBotService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class BaseTest {

    @MockBean
    public TelegramBotService telegramBotService;
}