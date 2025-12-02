package com.qmoney;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qmoney.model.PortfolioTrade;
import com.qmoney.model.TiingoCandle;
import com.qmoney.model.AnnualizedReturn;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PortfolioManagerApplication {

    private static final String TOKEN = "8c02a65d8db7603a9b3e6cc9d54c8a2b58763b6c";

    // ----------------------------- MODULE 1 -----------------------------
    public static File resolveFileFromResources(String filename) throws URISyntaxException {
        URL resource = PortfolioManagerApplication.class.getClassLoader().getResource(filename);
        if (resource == null) {
            throw new RuntimeException("File not found: " + filename);
        }
        return new File(resource.toURI());
    }

    public static List<PortfolioTrade> readTradesFromJson(String fileName)
            throws IOException, URISyntaxException {

        File inputFile = resolveFileFromResources(fileName);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        PortfolioTrade[] trades = mapper.readValue(inputFile, PortfolioTrade[].class);

        List<PortfolioTrade> list = new ArrayList<>();
        for (PortfolioTrade t : trades) {
            list.add(t);
        }
        return list;
    }

    // ----------------------------- MODULE 2 -----------------------------
    public static List<TiingoCandle> getStockQuotes(String symbol, LocalDate from, LocalDate to) {

        String url = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices"
                + "?startDate=" + from
                + "&endDate=" + to
                + "&token=" + TOKEN;

        RestTemplate restTemplate = new RestTemplate();
        TiingoCandle[] response = restTemplate.getForObject(url, TiingoCandle[].class);

        List<TiingoCandle> list = new ArrayList<>();
        if (response != null) {
            for (TiingoCandle c : response) {
                list.add(c);
            }
        }
        return list;
    }

    public static List<TiingoCandle> fetchCandles(PortfolioTrade trade, LocalDate endDate) {
        return getStockQuotes(trade.getSymbol(), trade.getPurchaseDate(), endDate);
    }

    // ----------------------------- MODULE 3 — PART A -----------------------------
    public static Double getOpeningPriceOnStartDate(List<TiingoCandle> candles) {
        return candles.get(0).getOpen();
    }

    public static Double getClosingPriceOnEndDate(List<TiingoCandle> candles) {
        return candles.get(candles.size() - 1).getClose();
    }

    // ----------------------------- MODULE 3 — PART B -----------------------------
    public static AnnualizedReturn calculateAnnualizedReturns(
            LocalDate endDate,
            PortfolioTrade trade,
            Double buyPrice,
            Double sellPrice) {

        // Absolute return
        double totalReturn = (sellPrice - buyPrice) / buyPrice;

        // Duration in years
        long totalDays = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
        double years = totalDays / 365.24;

        // Annualized return formula
        double annualized = Math.pow(1 + totalReturn, 1 / years) - 1;

        return new AnnualizedReturn(trade.getSymbol(), annualized, totalReturn);
    }

    // ----------------------------- MODULE 3 — PART C -----------------------------
    public static List<AnnualizedReturn> getAnnualizedReturn(
            List<PortfolioTrade> trades,
            LocalDate endDate) throws IOException {

        List<AnnualizedReturn> finalList = new ArrayList<>();

        for (PortfolioTrade trade : trades) {

            List<TiingoCandle> candles = fetchCandles(trade, endDate);

            if (candles.isEmpty()) {
                continue;
            }

            Double buyPrice = getOpeningPriceOnStartDate(candles);
            Double sellPrice = getClosingPriceOnEndDate(candles);

            AnnualizedReturn ar =
                    calculateAnnualizedReturns(endDate, trade, buyPrice, sellPrice);

            finalList.add(ar);
        }

        // Sort from highest annualized return to lowest
        Collections.sort(finalList, new Comparator<AnnualizedReturn>() {
            @Override
            public int compare(AnnualizedReturn a, AnnualizedReturn b) {
                return Double.compare(b.getAnnualizedReturn(), a.getAnnualizedReturn());
            }
        });

        return finalList;
    }

    // ----------------------------- MAIN FOR LOCAL TESTING -----------------------------
    public static void main(String[] args) throws Exception {
        List<PortfolioTrade> trades = readTradesFromJson("trades.json");
        LocalDate endDate = LocalDate.parse("2020-01-05");

        List<AnnualizedReturn> result = getAnnualizedReturn(trades, endDate);

        for (AnnualizedReturn ar : result) {
            System.out.println(ar);
        }
    }

}
