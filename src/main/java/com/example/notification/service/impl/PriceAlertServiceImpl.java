package com.example.notification.service.impl;

import com.example.notification.model.entity.PriceAlert;
import com.example.notification.repository.PriceAlertRepository;
import com.example.notification.service.PriceAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceAlertServiceImpl implements PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;
    @Override
    public PriceAlert save(PriceAlert priceAlert) {
        return priceAlertRepository.save(priceAlert);
    }
}