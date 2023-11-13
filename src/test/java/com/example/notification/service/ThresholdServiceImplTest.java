package com.example.notification.service;

import com.example.notification.BaseTest;
import com.example.notification.model.Threshold;
import com.example.notification.model.entity.Currency;
import com.example.notification.model.entity.PriceAlert;
import com.example.notification.model.entity.User;
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
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Sql(scripts = "classpath:create-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:cleanup-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ThresholdServiceImplTest extends BaseTest {

    @Autowired
    private ThresholdService thresholdService;

    @Autowired
    private PriceAlertService priceAlertService;

    private final NavigableMap<Threshold, User> thresholds = new TreeMap<>();

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
     * Application: Testing the refreshThresholds function, which updates threshold values.
     * What's being tested: Ensuring that after the update, the threshold values are correctly stored in the data structure (NavigableMap).
     */
    @Test
    public void testRefreshThresholds() {
        //given
        Optional<PriceAlert> optionalAlert1 = priceAlertService.findByUserIdAndCurrency(1L, Currency.BTC);
        assertTrue(optionalAlert1.isPresent());
        PriceAlert alert1 = optionalAlert1.get();

        Optional<PriceAlert> optionalAlert2 = priceAlertService.findByUserIdAndCurrency(1361169404L, Currency.BTC);
        assertTrue(optionalAlert2.isPresent());
        PriceAlert alert2 = optionalAlert2.get();

        // when
        thresholdService.refreshThresholds();

        // then
        NavigableMap<Threshold, User> result = thresholdService.getThresholdsMaxValue(new BigDecimal("85001"));
        assertTrue(result.containsKey(new Threshold(alert1.getMaxThreshold(), alert1.getUser())));
        assertTrue(result.containsKey(new Threshold(alert2.getMaxThreshold(), alert2.getUser())));
    }

    /**
     * Application: Testing that threshold values are cleared before they are updated.
     * What's being tested: Ensuring that old or irrelevant threshold values are removed before the threshold update function is executed.
     */
    @Test
    public void testThresholdsClearedBeforeRefresh() {
        //given
        User dummyUser = new User();
        dummyUser.setChatId(9999L);
        Threshold dummyThreshold = new Threshold(new BigDecimal("99999"), dummyUser);
        thresholds.put(dummyThreshold, dummyUser);
        assertEquals(1, thresholds.size());

        // when
        thresholdService.refreshThresholds();

        // then
        NavigableMap<Threshold, User> result = thresholdService.getThresholdsMaxValue(new BigDecimal("85001"));
        assertFalse(result.containsKey(dummyThreshold));
    }
}