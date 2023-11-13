package com.example.notification.service;

import com.example.notification.model.entity.Currency;
import com.example.notification.model.entity.PriceAlert;
import com.example.notification.model.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PriceAlertService {
    PriceAlert save(PriceAlert priceAlert);

    List<PriceAlert> findAll();

    Optional<PriceAlert> findByUserIdAndCurrency(Long chatId, Currency currencyName);

    void savePriceAlert(User user, Currency currency, BigDecimal maxThreshold);

    void createPriceAlert(Long userId, Currency currencyName);
}