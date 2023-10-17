package com.example.notificationservice.service.impl;

import com.example.notificationservice.service.NotificationService;
import com.example.notificationservice.service.ServiceException;
import com.example.notificationservice.webSocket.WebSocketClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USDT_JSON = "{\"method\": \"SUBSCRIPTION\", \"params\": [\"spot@public.deals.v3.api@BTCUSDT\"]}";
    private static final String EUR_JSON = "/ValCurs//Valute[@ID='6389.06534240']/Value";

    @Autowired
    private WebSocketClient client;

    @Override
    public String getUSDExchangeRate() throws ServiceException {
        try {
            String json = client.startListening().get(); // ожидание завершения асинхронной операции
            return extractCurrencyValueFromJson(json, USDT_JSON);
        } catch (InterruptedException | ExecutionException e) {
            throw new ServiceException("Error when getting exchange rate", e);
        }
    }

    public String extractCurrencyValueFromJson(String json, String jsonPath) throws ServiceException {
        try {
            JsonNode rootNode = objectMapper.readTree(json);

            if (rootNode.has("id") && rootNode.get("id").asInt() == 0 && rootNode.has("code") && rootNode.get("code").asInt() == 0) {
                return rootNode.get("msg").asText();
            }

            String[] parts = jsonPath.split("\\.");

            for (String part : parts) {
                if (part.endsWith("]") && part.contains("[")) {
                    int index = Integer.parseInt(part.substring(part.indexOf('[') + 1, part.indexOf(']')));
                    rootNode = rootNode.get(part.substring(0, part.indexOf('['))).get(index);
                } else {
                    rootNode = rootNode.get(part);
                }
                if (rootNode == null) {
                    throw new ServiceException("Invalid JSON or empty: " + json);
                }
            }
            return rootNode.asText();
        } catch (Exception e) {
            throw new ServiceException("Failed to parse JSON", e);
        }
    }
}