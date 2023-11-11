package com.example.notification.websocket;

import com.example.notification.BaseTest;
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

    @BeforeEach
    public void setup() {
        webSocketSteps = new WebSocketSteps();
        subscriptionSteps = new SubscriptionSteps();
    }

    @Test
    public void testWebSocketIntegration() throws InterruptedException {
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onMessage(okhttp3.WebSocket webSocket, String text) {
                mocktelegramBotService.sendMessage(1361169404L, "New message: " + text, null);
            }
        };

        WebSocket webSocket = webSocketSteps.connectToWebSocket(listener);

        subscriptionSteps.sendSubscription(webSocket);

        Thread.sleep(10000);

        verify(mocktelegramBotService, atLeastOnce()).sendMessage(anyLong(), startsWith("New message:"), isNull());
    }
}