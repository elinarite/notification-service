package com.example.notification.service;

import com.example.notification.model.entity.PriceAlert;

import java.util.List;
import java.util.Optional;

public interface PriceAlertService {
    PriceAlert save(PriceAlert priceAlert);

    List<PriceAlert> findAll();

    Optional<PriceAlert> findByUserIdAndCurrency(Long chatId, String currencyName);
}