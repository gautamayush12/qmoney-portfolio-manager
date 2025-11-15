package com.qmoney;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qmoney.client.TiingoClient;
import com.qmoney.model.PortfolioTrade;
import com.qmoney.model.TiingoCandle;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class PortfolioManagerApplication {

    // Load file from resources
    public static File resolveFileFromResources(String filename) throws URISyntaxException {
        URL resource = PortfolioManagerApplication.class.getClassLoader().getResource(filename);
        if(resource == null){
            throw new IllegalArgumentException("File Not Found: " + filename);
        } else {
            return new File(resource.toURI());
        }
    }

    // Read trades from JSON
    public static List<PortfolioTrade> readTradesFromJson(String fileName) throws IOException, URISyntaxException {
        File inputFile = resolveFileFromResources(fileName);
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        PortfolioTrade[] trades = om.readValue(inputFile, PortfolioTrade[].class);
        return Arrays.asList(trades);
    }

    // Fetch Tiingo candles
    public static List<TiingoCandle> fetchCandles(PortfolioTrade trade, LocalDate endDate, TiingoClient client){
        TiingoCandle[] candles = client.getStockQuote(trade.getSymbol(), trade.getPurchaseDate(), endDate);
        return Arrays.asList(candles);
    }

    // Sort trades by closing price
    public static void sortTradesByPrice(List<PortfolioTrade> trades, LocalDate endDate, TiingoClient client) {
        Collections.sort(trades, new Comparator<PortfolioTrade>() {
            @Override
            public int compare(PortfolioTrade t1, PortfolioTrade t2) {
                double price1 = client.getStockQuote(t1.getSymbol(), t1.getPurchaseDate(), endDate)[0].getClose();
                double price2 = client.getStockQuote(t2.getSymbol(), t2.getPurchaseDate(), endDate)[0].getClose();
                return Double.compare(price1, price2);
            }
        });
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        TiingoClient client = new TiingoClient();
        LocalDate endDate = LocalDate.now();

        List<PortfolioTrade> trades = readTradesFromJson("trades.json");

        // Print trades
        System.out.println("Trades from JSON:");
        for(PortfolioTrade trade : trades){
            System.out.println(trade);
        }

        // Sort trades by closing price
        sortTradesByPrice(trades, endDate, client);
        System.out.println("\nTrades sorted by price:");
        for(PortfolioTrade trade : trades){
            System.out.println(trade.getSymbol());
        }

        // Fetch candles for first trade (example)
        if(!trades.isEmpty()){
            List<TiingoCandle> candles = fetchCandles(trades.get(0), endDate, client);
            System.out.println("\nCandles for " + trades.get(0).getSymbol() + ":");
            for(TiingoCandle c : candles){
                System.out.println(c);
            }
        }
    }
}
