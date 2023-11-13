package com.example.notification.bot;

import com.example.notification.api.MexcService;
import com.example.notification.bot.config.TelegramBotConfig;
import com.example.notification.model.entity.Currency;
import com.example.notification.model.entity.User;
import com.example.notification.service.NotificationService;
import com.example.notification.service.PriceAlertService;
import com.example.notification.service.UserService;
import com.example.notification.service.impl.ReplyKeyboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Slf4j
@Component
public class TelegramBotService extends TelegramLongPollingBot {
    private static final String START = "/start";
    private static final String BTC = "BTC";
    private static final String ETH = "ETH";
    private static final String LTC = "LTC";
    private static final String DNX = "DNX";
    private static final String RETURN = "Return";
    private static final String MIN = "MIN";
    private static final String MAX = "MAX";

    @Autowired
    private TelegramBotConfig telegramBot;

    @Autowired
    private UserService userService;

    @Autowired
    private PriceAlertService priceAlertService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MexcService mexcService;
    //
    @Autowired
    private ReplyKeyboardService replyKeyboardService;

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        if (msg == null || !msg.hasText()) return;

        String message = msg.getText();
        Long chatId = msg.getChatId();

        if (START.equals(message)) {
            startCommand(chatId, msg.getChat().getUserName(), msg.getChat().getFirstName(), replyKeyboardService.createStartKeyboard());
        } else if (Arrays.asList(BTC, ETH, LTC, DNX).contains(message)) {
            Currency currency = Currency.valueOf(message);
            getAverageCurrencyPrice(chatId, currency.getTradingPar(), currency);
        } else if (RETURN.equals(message)) {
            returnToStart(chatId);
        } else if (MAX.equals(message)) {
                notificationService.setBtcThreshold(chatId, message);
            } else {
                unknownCommand(chatId, replyKeyboardService.createStartKeyboard());
            }
        }

        private void startCommand(Long chatId, String userName, String firstName, ReplyKeyboardMarkup replyKeyboard) {
        sendWelcomeMessage(chatId, userName, replyKeyboard);
        if (!isUserRegistered(chatId)) {
            registerUser(chatId, userName, firstName);
        }
        sendMessage(chatId, TelegramMessage.SELECT_OPTION, replyKeyboardService.createStartKeyboard());
    }

    private void getAverageCurrencyPrice(Long chatId, String tradingPaar, Currency currency) {
        String avgPrice = mexcService.getAvgPrice(tradingPaar);
        sendMessage(chatId, String.format(TelegramMessage.GET_AVERAGE_PRICE, currency, avgPrice), replyKeyboardService.createMinMaxReturnKeyboard());
        priceAlertService.createPriceAlert(chatId, currency);
    }

    private void returnToStart(Long chatId) {
        sendMessage(chatId, TelegramMessage.RETURN_MENU, replyKeyboardService.createStartKeyboard());
    }

    public void sendMessage(Long chatId, String message, ReplyKeyboardMarkup replyKeyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(replyKeyboard);
        executeMessage(sendMessage);
    }

    private void sendWelcomeMessage(Long chatId, String userName, ReplyKeyboardMarkup replyKeyboard) {
        String formattedText = String.format(TelegramMessage.WELCOME_MESSAGE, userName);
        sendMessage(chatId, formattedText, replyKeyboard);
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

    private void unknownCommand(Long chatId, ReplyKeyboardMarkup replyKeyboard) {
        String text = "Could not recognize the command!";
        sendMessage(chatId, text, replyKeyboard);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("ERROR_TEXT" + e.getMessage());
        }
    }
}