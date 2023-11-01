package com.example.notification.bot;

import com.example.notification.bot.config.TelegramBotConfig;
import com.example.notification.model.entity.User;
import com.example.notification.repository.NotificationService;
import com.example.notification.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBotService extends TelegramLongPollingBot {
    private static final String START = "/start";
    private static final String BTC = "/btc";
    private static final String ETH = "/eth";
    private static final String LTC = "/ltc";
    private static final String DNX = "/dnx";

    @Autowired
    private TelegramBotConfig telegramBot;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        if (msg == null || !msg.hasText()) return;

        String message = msg.getText();
        Long chatId = msg.getChatId();

        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                String firstName = update.getMessage().getChat().getFirstName();
                startCommand(chatId, userName, firstName);
            }
            default -> {
                if (message.startsWith(BTC)) {
                    notificationService.setBtcThreshold(chatId, message);
                } else {
                    unknownCommand(chatId);
                }
            }
        }
    }

    private void startCommand(Long chatId, String userName, String firstName) {
        sendWelcomeMessage(chatId, userName);
        if (isUserRegistered(chatId)) {
        } else {
            registerUser(chatId, userName, firstName);
        }
    }

    private void sendWelcomeMessage(Long chatId, String userName) {
        String formattedText = String.format(TelegramMessage.WELCOME_MESSAGE, userName);
        sendMessage(chatId, formattedText);
    }

    private boolean isUserRegistered(Long chatId) {
        return userService.findByChatId(chatId).isPresent();
    }

    private void registerUser(Long chatId, String userName, String firstName) {
        if (userService.findByChatId(chatId).isEmpty()) {
            User user = new User();
            user.setChatId(chatId);
            user.setUserName(userName);
            user.setFirstName(firstName);
            userService.saveUser(user);
            log.info("user saved: " + user);
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
            log.error("Error sending message", e);
        }
    }
}