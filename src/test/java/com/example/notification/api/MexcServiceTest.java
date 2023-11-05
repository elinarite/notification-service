package com.example.notification.api;

import com.example.notification.BaseTest;
import com.example.notification.bot.TelegramMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
public class MexcServiceTest extends BaseTest {

    @Autowired
    private MexcService mexcService;

    /**
     * Application: Testing the correctness of retrieving the average price for the currency through the API.
     * What's being tested: The API successfully retrieves the average price, and the obtained result is not null or empty.
     */
    @Test
    public void testGetAvgPrice() {
        //Given
        String symbol = "BTCUSD";

        //When
        String price = mexcService.getAvgPrice(symbol);

        //Then
        assertNotNull(price);
        assertFalse(price.isEmpty());
    }

    /**
     * Application: Testing the handling of API errors when retrieving the average price for the currency.
     * What's being tested: The system correctly handles API errors and returns the expected error message when trying to retrieve the average price for a specific currency.
     */
    @Test
    public void testGetAvgPriceApiError() {
        //Given
        RestTemplate restTemplate = new RestTemplate() {
            @Override
            public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
                throw new RestClientException(TelegramMessage.API_SERVICE_UNAVAILABLE);
            }
        };
        String symbol = "BTCUSDT";

        //When
        MexcService mexcServiceWithApiError = new MexcService(restTemplate);
        String result = mexcServiceWithApiError.getAvgPrice(symbol);

        //Then
        assertEquals(TelegramMessage.API_SERVICE_UNAVAILABLE, result);
    }
}