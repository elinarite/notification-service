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
}