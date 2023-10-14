import com.example.notificationservice.Service.impl.TelegramBotService;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import steps.SubscriptionSteps;
import steps.WebSocketSteps;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = WebSocketClientTest.class)
public class WebSocketClientTest {
    private WebSocketSteps webSocketSteps;
    private SubscriptionSteps subscriptionSteps;
    @Mock
    private TelegramBotService mockTelegramBotService;

    @BeforeEach
    public void setup() {
        webSocketSteps = new WebSocketSteps();
        subscriptionSteps = new SubscriptionSteps();
        mockTelegramBotService = mock(TelegramBotService.class);
    }

    @Test
    public void testWebSocketIntegration() throws InterruptedException {
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onMessage(okhttp3.WebSocket webSocket, String text) {
                mockTelegramBotService.sendMessage(1361169404L, "New message: " + text);
            }
        };

        WebSocket webSocket = webSocketSteps.connectToWebSocket(listener);

        subscriptionSteps.sendSubscription(webSocket);

        Thread.sleep(10000);

        verify(mockTelegramBotService, atLeastOnce()).sendMessage(anyLong(), startsWith("New message:"));
    }
}