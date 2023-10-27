package com.example.notification;


import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import steps.SubscriptionSteps;
import steps.WebSocketSteps;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class WebSocketClientTest extends BaseTest {
    private WebSocketSteps webSocketSteps;
    private SubscriptionSteps subscriptionSteps;

//    @MockBean
//    private TelegramBotService mockTelegramBotService;

    @BeforeEach
    public void setup() {
        webSocketSteps = new WebSocketSteps();
        subscriptionSteps = new SubscriptionSteps();
//        mockTelegramBotService = mock(TelegramBotService.class);
    }

    @Test
    public void testWebSocketIntegration() throws InterruptedException {
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onMessage(okhttp3.WebSocket webSocket, String text) {
                telegramBotService.sendMessage(1361169404L, "New message: " + text);
            }
        };

        WebSocket webSocket = webSocketSteps.connectToWebSocket(listener);

        subscriptionSteps.sendSubscription(webSocket);

        Thread.sleep(10000);

        verify(telegramBotService, atLeastOnce()).sendMessage(anyLong(), startsWith("New message:"));
    }
}
