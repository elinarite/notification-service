package com.example.notification.model.entity;

public enum Currency {
    BTC("BTCUSDT"),
    ETH("ETHUSDT"),
    LTC("LTCUSDT"),
    DNX("DNXUSDT");

    private final String tradingPar;

    Currency(String tradingPar) {
        this.tradingPar = tradingPar;
    }

    public String getTradingPar(){
        return tradingPar;
    }
}