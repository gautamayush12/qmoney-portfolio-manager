package com.qmoney.service;

public class PortfolioManagerFactory {

    public static PortfolioManager getPortfolioManager(String token) {
        StockQuotesService service = new TiingoService(token);
        return new PortfolioManagerImpl(service);
    }
}
