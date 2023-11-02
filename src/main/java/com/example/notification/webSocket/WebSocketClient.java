package com.example.notification.webSocket;

import com.example.notification.bot.TelegramBotService;
import com.example.notification.dto.WebSocketResponse;
import com.example.notification.repository.NotificationService;
import com.example.notification.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class WebSocketClient {

    @Autowired
    private OkHttpClient client;

    @Autowired
    TelegramBotService telegramBotService;

    @Autowired
    private NotificationService notificationService;

    @Value("${mexc.currency.rates.url}")
    private String url;
    private WebSocket webSocket;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            startListening();

        } catch (ServiceException e) {
            log.info("Error starting WebSocket listener: ", e);
        }
    }

    public CompletableFuture<String> startListening() throws ServiceException {
        CompletableFuture<String> futureMessage = new CompletableFuture<>();

        Request request = new Request.Builder()
                .url(url)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                log.info("Received message via WebSocket: WebSocketClient.onOpen");
                String subscriptionMessage = "{\"method\": \"SUBSCRIPTION\", \"params\": [\"spot@public.deals.v3.api@BTCUSDT\"]}";
                webSocket.send(subscriptionMessage);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    WebSocketResponse response = objectMapper.readValue(text, WebSocketResponse.class);
                    BigDecimal currentValue = new BigDecimal(response.getD().getDeals().get(0).getP());
                    notificationService.processCurrencyValue(currentValue);
                } catch (IOException e) {
                    log.error("Failed to parse WebSocket message: " + text, e);
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                log.error("Error with WebSocket", t);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                log.info("WebSocket closes with code " + code + ": " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                log.info("WebSocket closed with code " + code + ": " + reason);
            }
        });
        return futureMessage;
    }

    public void stopListening() {
        if (webSocket != null) {
            webSocket.close(1000, "Closing");
        }
    }
}