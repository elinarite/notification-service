package com.example.notification.model.entity;

public enum Currency {
    BTC("BTCUSDT"),
    ETH("ETHUSDT"),
    LTC("LTCUSDT"),
    DNX("DNXUSDT");

    private final String tradingPaar;

    Currency(String tradingPar) {
        this.tradingPaar = tradingPar;
    }

    public String getTradingPar(){
        return tradingPaar;
    }
}