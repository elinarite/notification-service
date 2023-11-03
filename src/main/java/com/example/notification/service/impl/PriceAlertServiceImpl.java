package com.example.notification.service.impl;

import com.example.notification.model.entity.PriceAlert;
import com.example.notification.repository.PriceAlertRepository;
import com.example.notification.service.PriceAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceAlertServiceImpl implements PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;

    @Override
    public PriceAlert save(PriceAlert priceAlert) {
        return priceAlertRepository.save(priceAlert);
    }

    public Optional<PriceAlert> findByUserIdAndCurrency(Long chatId, String currency) {
        return priceAlertRepository.findByUserIdAndCurrency(chatId, currency);
    }

    @Override
    public List<PriceAlert> findAll() {
        return priceAlertRepository.findAll();
    }
}