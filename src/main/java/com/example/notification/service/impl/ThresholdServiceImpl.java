package com.example.notification.service.impl;

import com.example.notification.model.Threshold;
import com.example.notification.model.entity.PriceAlert;
import com.example.notification.model.entity.User;
import com.example.notification.service.PriceAlertService;
import com.example.notification.service.ThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

@Service
public class ThresholdServiceImpl implements ThresholdService {

    @Autowired
    private PriceAlertService priceAlertService;

    private final NavigableMap<Threshold, User> thresholds = new TreeMap<>();

    @Scheduled(fixedRate = 60000)
    public void refreshThresholds() {
        thresholds.clear();
        List<PriceAlert> priceAlerts = priceAlertService.findAll();
        for (PriceAlert priceAlert : priceAlerts) {
            User user = priceAlert.getUser();
            BigDecimal value = priceAlert.getMaxThreshold();
            Threshold threshold = new Threshold(value, user);
            thresholds.put(threshold, user);
        }
    }

    public NavigableMap<Threshold, User> getThresholdsMaxValue(BigDecimal value) {
        return thresholds.headMap(new Threshold(value), true);
    }
}