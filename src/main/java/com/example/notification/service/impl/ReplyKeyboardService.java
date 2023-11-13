package com.example.notification.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReplyKeyboardService {
    private static final KeyboardButton BTC = new KeyboardButton("BTC");
    private static final KeyboardButton ETH = new KeyboardButton("ETH");
    private static final KeyboardButton LTC = new KeyboardButton("LTC");
    private static final KeyboardButton DNX = new KeyboardButton("DNX");
    private static final KeyboardButton RETURN_MIN_MAX = new KeyboardButton("Return");
    private static final KeyboardButton RETURN_MIN = new KeyboardButton("Return");
    private static final KeyboardButton RETURN_MAX = new KeyboardButton("Return");
    private static final KeyboardButton RETURN_NEW_DELETE_MIN = new KeyboardButton("Return");
    private static final KeyboardButton RETURN_NEW_DELETE_MAX = new KeyboardButton("Return");
    private static final KeyboardButton MIN_PRICE_LIMIT = new KeyboardButton("Min price");
    private static final KeyboardButton MIN_PRICE_LIMIT_DELETE = new KeyboardButton("Min price delete");
    private static final KeyboardButton MIN_PRICE_LIMIT_NEW = new KeyboardButton("New min price limit");
    private static final KeyboardButton MAX_PRICE_LIMIT = new KeyboardButton("Max price");
    private static final KeyboardButton MAX_PRICE_LIMIT_DELETE = new KeyboardButton("MAX price delete");
    private static final KeyboardButton MAX_PRICE_LIMIT_NEW = new KeyboardButton("New min price limit");

    private ReplyKeyboardMarkup createKeyboardMarkup(List<List<KeyboardButton>> rows) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (List<KeyboardButton> row : rows) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.addAll(row);
            keyboardRows.add(keyboardRow);
        }

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup createStartKeyboard() {
        return createKeyboardMarkup(
                Arrays.asList(
                        Arrays.asList(BTC, ETH),
                        Arrays.asList(LTC, DNX)
                )
        );
    }

    public ReplyKeyboardMarkup createMinMaxReturnKeyboard() {
        return createKeyboardMarkup(
                List.of(
                        Arrays.asList(MAX_PRICE_LIMIT, MIN_PRICE_LIMIT, RETURN_MIN_MAX)
                )
        );
    }
}