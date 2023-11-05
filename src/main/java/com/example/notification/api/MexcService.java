package com.example.notification.api;

import com.example.notification.dto.ApiResponse;
import com.example.notification.bot.TelegramMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MexcService {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "https://api.mexc.com";
    private final String AVERAGE_PRICE_URL = "/api/v3/avgPrice?symbol=";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAvgPrice(String symbol) {
        try {
            String response = restTemplate.getForObject(BASE_URL + AVERAGE_PRICE_URL + symbol, String.class);

            ApiResponse apiResponse = objectMapper.readValue(response, ApiResponse.class);
            return apiResponse.getPrice();
        } catch (JsonProcessingException e) {
            log.error(TelegramMessage.ERROR_GET_AVERAGE_PRICE, e);
            return TelegramMessage.ERROR_GET_AVERAGE_PRICE;
        } catch (RestClientException e) {
            log.error(TelegramMessage.API_SERVICE_UNAVAILABLE, e);
            return TelegramMessage.API_SERVICE_UNAVAILABLE;
        }
    }
}