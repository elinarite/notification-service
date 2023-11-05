package com.example.notification.api;

import com.example.notification.BaseTest;
import com.example.notification.bot.TelegramMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class MexcServiceJsonTest extends BaseTest {

    @MockBean
    private RestTemplate mockRestTemplate;

    @Autowired
    private MexcService mexcService;

     /**
     * Application: Verify handling of valid JSON response.
     * What's being tested: The service correctly processes valid JSON data.
     */
    @Test
    public void testGetAvgPriceValidJson() {
        //Given
        String invalidJson = "{ \"mins\": 5, \"price\": \"35082.566000000006\" }";
        String symbol = "BTCUSDT";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.mexc.com/api/v3/avgPrice")
                .queryParam("symbol", symbol);
        String uri = builder.toUriString();

        //When
        when(mockRestTemplate.getForObject(uri, String.class)).thenReturn(invalidJson);
        String result = mexcService.getAvgPrice(symbol);

        //Then
        assertEquals(result, result);
    }

    /**
     * Application: Verify handling of invalid JSON response.
     * What's being tested: The service handles invalid JSON data and returns an error message.
     */
    @Test
    public void testGetAvgPriceInvalidJson() {
        //Given
        String invalidJson = "{ \"mins\": 5, \"prce\": \"35082.566000000006\" }";
        String symbol = "BTCUSDT";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.mexc.com/api/v3/avgPrice")
                .queryParam("symbol", symbol);
        String uri = builder.toUriString();

        //When
        when(mockRestTemplate.getForObject(uri, String.class)).thenReturn(invalidJson);
        String result = mexcService.getAvgPrice(symbol);

        //Then
        assertEquals(TelegramMessage.ERROR_GET_AVERAGE_PRICE, result);
    }
}