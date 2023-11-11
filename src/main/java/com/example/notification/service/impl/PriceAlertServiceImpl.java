package com.example.notification.service.impl;

import com.example.notification.model.entity.Currency;
import com.example.notification.model.entity.PriceAlert;
import com.example.notification.model.entity.User;
import com.example.notification.repository.PriceAlertRepository;
import com.example.notification.repository.UserRepository;
import com.example.notification.service.PriceAlertService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceAlertServiceImpl implements PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;

    private final UserRepository userRepository;

    @Override
    public PriceAlert save(PriceAlert priceAlert) {
        return priceAlertRepository.save(priceAlert);
    }

    @Override
    public Optional<PriceAlert> findByUserIdAndCurrency(Long chatId, Currency currency) {
        return priceAlertRepository.findByUserIdAndCurrency(chatId, currency);
    }

    @Override
    public List<PriceAlert> findAll() {
        return priceAlertRepository.findAll();
    }

    @Override
    public void savePriceAlert(User user, Currency currency, BigDecimal maxThreshold) {
        Optional<PriceAlert> existingAlertOpt = findByUserIdAndCurrency(user.getChatId(), currency);

        PriceAlert priceAlert;
        if (existingAlertOpt.isPresent()) {
            priceAlert = existingAlertOpt.get();
        } else {
            priceAlert = new PriceAlert();
            priceAlert.setUser(user);
            priceAlert.setCurrencyName(currency);
        }

        priceAlert.setMaxThreshold(maxThreshold);
        save(priceAlert);
    }

    @Transactional
    public void createPriceAlert(Long userId, Currency currencyName) {
        User user = userRepository.findByChatId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        PriceAlert alert = new PriceAlert();
        alert.setUser(user);
        alert.setCurrencyName(currencyName);
        alert.setMinThreshold(null);
        alert.setMaxThreshold(null);
        save(alert);
    }
}