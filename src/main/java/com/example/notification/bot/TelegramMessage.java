package com.example.notification.bot;

public class TelegramMessage {

    public static final String WELCOME_MESSAGE = """
            Welcome to the bot, %s!

            Here you can get the current cryptocurrency exchange rate in relation to the US dollar, 
            set by the Mexc stock exchange, and set threshold priceAlerts.
                 
            When the price exceeds your limit, you will receive a priceAlert.

            Choose the cryptocurrency for which you want to set a limit and specify the threshold.
            Example: /btc 25000
            /btc - Bitcoin exchange rate
            /eth - Ethereum exchange rate
            /ltc - Litecoin exchange rate
            /dnx - Dynex exchange rate

            Additional commands:
            /help - get help
            """;

    public static final String NEW_MAX_THRESHOLD_VALUE = "You have established a maximum threshold for the currency %s: %s";
    public static final String NEW_MIN_THRESHOLD_VALUE = "You have established a minimum threshold for the currency %s: %s";
    public static final String EXCEEDING_MAX_THRESHOLD_VALUE = "Your maximum threshold value has been exceeded, current currency rate: %s";
    public static final String EXCEEDING_MIN_THRESHOLD_VALUE = "Your minimum threshold value has been exceeded, current currency rate: %s";
    public static final String ERROR_OCCURRED_PLEASE_TRY_AGAIN_LATER = "An error occurred. Please try again later";
    public static final String ERROR_SETTING_LIMIT_PLEASE_CHECK_FORMAT = "An error occurred while setting the limit. Please check the command format.";
    public static final String ERROR_GET_AVERAGE_PRICE = "Error when retrieving the price.";
    public static final String GET_AVERAGE_PRICE = "Average price for the currency %s: %s";
    public static final String API_SERVICE_UNAVAILABLE = "Failed to fetch data from Mexc API.";
}