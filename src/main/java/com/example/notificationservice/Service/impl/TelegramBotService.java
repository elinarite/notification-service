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

//
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

//    @Override
//    public String getBotUsername() {
//        return telegramBot.getBotName();
//    }

    @Override
    public String getBotUsername() {
        return "notification_service_sbot";
    }
    @Override
    public String getBotToken() {
        return telegramBot.getToken();
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                Добро пожаловать в бот, %s!

                Здесь Вы сможете узнать официальные курсы валют на сегодня, установленные ЦБ РФ.

                Для этого воспользуйтесь командами:
                /usd - курс доллара
                /eur - курс евро

                Дополнительные команды:
                /help - получение справки
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void usdCommand(Long chatId) {
        String formattedText;
        try {
            var usd = exchangeRatesService.getUSDExchangeRate();
            var text = "Курс доллара на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);
        } catch (ServiceException e) {
            LOG.error("Ошибка получения курса доллара", e);
            formattedText = "Не удалось получить текущий курс доллара. Попробуйте позже.";
        }
        sendMessage(chatId, formattedText);
    }

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }

//    public void sendMessage(Long chatId, String text) {
//        var chatIdStr = String.valueOf(chatId);
//        var sendMessage = new SendMessage(chatIdStr, text);
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            LOG.error("Ошибка отправки сообщения", e);
//        }
//    }

    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
            ;
        }
    }
}