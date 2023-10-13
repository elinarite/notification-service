package com.example.notificationservice.webSocket;


import com.example.notificationservice.Service.impl.TelegramBotService;
import com.example.notificationservice.bot.TelegramBotConfig;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.notificationservice.Service.ServiceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;


@Component
public class WebSocketClient {

    @Autowired
    private OkHttpClient client;

    @Autowired
    TelegramBotService telegramBotService;

    @Value("${mexc.currency.rates.url}")
    private String url;

    private WebSocket webSocket;
//    private WebSocketClient webSocket;
    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotConfig.class);

//    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @PostConstruct
    public void init() {
        try {
            startListening();

        } catch (ServiceException e) {
            // Обработка исключения, например, логирование
            System.err.println("Ошибка при запуске прослушивания WebSocket: " + e.getMessage());
        }
    }

    public CompletableFuture<String> startListening() throws ServiceException {
        CompletableFuture<String> futureMessage = new CompletableFuture<>();

        Request request = new Request.Builder()
                .url(url)
                .build();

//        webSocket = client.newWebSocket(request, new WebSocketListener() {
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                LOG.info("Получено сообщение через WebSocket: WebSocketClient.onOpen");
                String subscriptionMessage = "{\"method\": \"SUBSCRIPTION\", \"params\": [\"spot@public.deals.v3.api@BTCUSDT\"]}";
                webSocket.send(subscriptionMessage);
            }


            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // Когда получаем новое сообщение, отправляем его в Telegram
                telegramBotService.sendMessage(1361169404L, "Новое значение: " + text);
                LOG.info("Получено сообщение через WebSocket: " + text);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                LOG.error("Ошибка при работе с WebSocket", t);

            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
//                LOG.info("WebSocket закрывается с кодом " + code + ": " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
//                LOG.info("WebSocket закрыт с кодом " + code + ": " + reason);
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
