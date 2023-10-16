package com.example.notificationservice.Service.impl;

import com.example.notificationservice.Service.NotificationService;
import com.example.notificationservice.Service.ServiceException;
import com.example.notificationservice.bot.TelegramBotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;

@Component
public class TelegramBotService extends TelegramLongPollingBot {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotService.class);
    private static final String START = "/start";
    private static final String USD = "/usd";

    @Autowired
    private TelegramBotConfig telegramBot;

    @Autowired
    private NotificationService exchangeRatesService;

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case USD -> usdCommand(chatId);
            default -> unknownCommand(chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBot.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramBot.getToken();
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                Welcome to the bot, %s!
                           
                Here you can find out the official exchange rates for today, set by Mexc stock exchange
                           
                To do this, use the commands:
                /usd - dollar exchange rate
                /eur - euro exchange rate
                           
                Additional commands:
                /help - get help
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void usdCommand(Long chatId) {
        String formattedText;
        try {
            var usd = exchangeRatesService.getUSDExchangeRate();
            var text = "The dollar exchange rate at %s is %s btc";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("Error getting dollar exchange rate", e);
            formattedText = "Could not get the current dollar rate. Try later..";
        }
        sendMessage(chatId, formattedText);
    }

    private void unknownCommand(Long chatId) {
        var text = "Could not recognize the command!";
        sendMessage(chatId, text);
    }

    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Error sending message", e);
        }
    }
}