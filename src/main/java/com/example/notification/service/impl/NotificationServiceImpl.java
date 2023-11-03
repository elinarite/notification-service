package com.example.notification.service.impl;

import com.example.notification.bot.TelegramBotService;
import com.example.notification.bot.TelegramMessage;
import com.example.notification.model.Threshold;
import com.example.notification.model.entity.PriceAlert;
import com.example.notification.model.entity.User;
import com.example.notification.service.NotificationService;
import com.example.notification.service.PriceAlertService;
import com.example.notification.service.ThresholdService;
import com.example.notification.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NavigableMap;
import java.util.Optional;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UserService userService;

    @Autowired
    private PriceAlertService priceAlertService;

    @Autowired
    TelegramBotService telegramBotService;

    @Autowired
    private ThresholdService thresholdService;

    public void setBtcThreshold(Long chatId, String message) {
        try {
            String currency = message.split(" ")[0];
            BigDecimal maxThreshold = new BigDecimal(message.split(" ")[1]);

            Optional<User> userOptional = userService.findByChatId(chatId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                savePriceAlert(user, currency, maxThreshold);
                telegramBotService.sendMessage(chatId, String.format(TelegramMessage.NEW_MAX_THRESHOLD_VALUE, currency, maxThreshold));
            } else {
                telegramBotService.sendMessage(chatId, TelegramMessage.ERROR_OCCURRED_PLEASE_TRY_AGAIN_LATER);
            }
        } catch (Exception e) {
            telegramBotService.sendMessage(chatId, TelegramMessage.ERROR_SETTING_LIMIT_PLEASE_CHECK_FORMAT);
        }
    }

    private void savePriceAlert(User user, String currency, BigDecimal maxThreshold) {
        PriceAlert priceAlert = new PriceAlert();
        priceAlert.setUser(user);
        priceAlert.setCurrencyName(currency);
        priceAlert.setMaxThreshold(maxThreshold);
        priceAlertService.save(priceAlert);
    }

    public void processCurrencyValue(BigDecimal currentValue) {
        log.info("Processing currency value: " + currentValue);
        NavigableMap<Threshold, User> headMap = thresholdService.getThresholdsMaxValue(currentValue);
        for (Threshold threshold : headMap.keySet()) {
            User user = threshold.getUser();
            telegramBotService.sendMessage(user.getChatId(), String.format(TelegramMessage.EXCEEDING_MAX_THRESHOLD_VALUE, currentValue));
        }
    }
}