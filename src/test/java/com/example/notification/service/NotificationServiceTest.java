package com.example.notification.service;

import com.example.notification.BaseTest;
import com.example.notification.model.entity.PriceAlert;
import com.example.notification.model.entity.User;
import com.example.notification.repository.NotificationService;
import com.example.notification.service.impl.ThresholdService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Sql(scripts = "classpath:create-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:cleanup-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class NotificationServiceTest extends BaseTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PriceAlertService priceAlertService;

    @Autowired
    private ThresholdService thresholdService;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.11")
            .withDatabaseName("test")
            .withUsername("testuser")
            .withPassword("testpassword");

    /**
     * Application: Checking the status of the PostgreSQL test container.
     * What's being tested: Ensuring that the container has been created and is currently running.
     */
    @Test
    void connectionEstablished() {
        Assertions.assertTrue(postgreSQLContainer.isCreated(), "Container should be created");

        Assertions.assertTrue(postgreSQLContainer.isRunning(), "Container should be running");
    }

    /**
     * Application: Testing the correctness of setting the threshold value for the BTC currency.
     * What's being tested: Ensuring that the threshold value is successfully saved and the correct notification is sent to the user.
     */
    @Test
    public void testSetBtcThresholdDataExtraction() {
        // Given
        Long chatId = 1L;
        String message = "/btc 24000";
        String currency = "/btc";

        // When
        notificationService.setBtcThreshold(chatId, message);

        // Then
        Optional<PriceAlert> savedAlerts = priceAlertService.findByUserIdAndCurrency(chatId, currency);
        assertTrue(savedAlerts.isPresent());

        PriceAlert savedAlert = savedAlerts.get();

        assertEquals("/btc", savedAlert.getCurrencyName());
        assertEquals(new BigDecimal("24000.00"), savedAlert.getMaxThreshold());
        verify(mocktelegramBotService).sendMessage(chatId, "New currency value for /btc: 24000");
    }

    /**
     * Application: Testing the behavior when setting a threshold for a non-existent chat ID.
     * What's being tested: Ensuring that the threshold value was not saved and a correct error notification was sent.
     */
    @Test
    public void setBtcThresholdNonExistentChatId() {
        // Given
        Long nonExistentChatId = 12345L;
        String message = "/btc 24000";
        String currency = "/btc";

        // When
        notificationService.setBtcThreshold(nonExistentChatId, message);

        // Then
        Optional<PriceAlert> savedAlerts = priceAlertService.findByUserIdAndCurrency(nonExistentChatId, currency);
        assertFalse(savedAlerts.isPresent());
        verify(mocktelegramBotService).sendMessage(nonExistentChatId, "An error occurred. Please try again later.");
    }

    /**
     * Application: Testing the behavior when an incorrect message format is provided for setting the threshold.
     * What's being tested: Ensuring that the threshold value was not saved and a correct format error notification was sent.
     */
    @Test
    public void setBtcThresholdIncorrectMessageFormat() {
        // Given
        Long chatId = 1L;
        String incorrectMessage = "/btcincorrect 24000";
        String currency = "/btc";
        String message = "An error occurred while setting the limit. Please check the command format.";

        boolean isAlertPresentInitially = priceAlertService.findByUserIdAndCurrency(chatId, currency).isPresent();

        // When
        notificationService.setBtcThreshold(chatId, incorrectMessage);

        // Then
        boolean isAlertPresentAfterTest = priceAlertService.findByUserIdAndCurrency(chatId, currency).isPresent();
        assertFalse(isAlertPresentInitially && isAlertPresentAfterTest);
        verify(mocktelegramBotService).sendMessage(chatId, message);
    }

    /**
     * Application: Testing the correctness of sending notifications when reaching the set currency threshold value.
     * What's being tested: Ensuring that when the threshold value is reached, the correct notification is sent to the user.
     */
    @Test
    public void processCurrencyValueNotificationSent() {
        // Given
        BigDecimal currentValue = new BigDecimal("16000");

        User user = userService.findByChatId(1361169404L).orElseThrow(() -> new RuntimeException("User not found"));

        thresholdService.refreshThresholds();

        // When
        notificationService.processCurrencyValue(currentValue);

        // Then
        verify(mocktelegramBotService).sendMessage(user.getChatId(), "New currency value: " + currentValue);
    }

    /**
     * Application: Testing the absence of a notification being sent if the current currency value hasn't reached the threshold.
     * What's being tested: Ensuring that no notification is sent to the user if the current currency value hasn't reached the set threshold.
     */
    @Test
    public void processCurrencyValueNoNotificationSent() {
        // Given
        BigDecimal currentValue = new BigDecimal("16000");
        User user = userService.findByChatId(1L).orElseThrow(() -> new RuntimeException("User not found"));

        thresholdService.refreshThresholds();

        // When
        notificationService.processCurrencyValue(currentValue);

        // Then
        verify(mocktelegramBotService, never()).sendMessage(user.getChatId(), "New currency value: " + currentValue);
    }
}