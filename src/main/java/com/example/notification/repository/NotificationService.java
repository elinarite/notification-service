package com.example.notification.repository;

import java.math.BigDecimal;

public interface NotificationService {
    void setBtcThreshold(Long chatId, String message);
    void processCurrencyValue(BigDecimal currentValue);
}