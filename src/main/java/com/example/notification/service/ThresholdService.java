package com.example.notification.service;

import com.example.notification.model.Threshold;
import com.example.notification.model.entity.User;

import java.math.BigDecimal;
import java.util.NavigableMap;

public interface ThresholdService {

    void refreshThresholds();

    NavigableMap<Threshold, User> getThresholdsMaxValue(BigDecimal value);
}