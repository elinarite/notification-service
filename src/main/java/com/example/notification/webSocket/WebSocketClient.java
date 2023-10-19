package com.example.notification.webSocket;

import com.example.notification.service.ServiceException;
import com.example.notification.service.impl.TelegramBotService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class WebSocketClient {

    @Autowired
    private OkHttpClient client;

    @Autowired
    TelegramBotService telegramBotService;

    @Value("${mexc.currency.rates.url}")
    private String url;

    private WebSocket webSocket;

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
                telegramBotService.sendMessage(1361169404L, "Новое значение: " + text);
                log.info("Received message via WebSocket: " + text);
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