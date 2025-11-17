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

    private static final String TOKEN = "YOUR_TIINGO_TOKEN_HERE";

    // ------------------ Helper: Load file ------------------
    public static File resolveFileFromResources(String filename) throws URISyntaxException {
        URL resource = PortfolioManagerApplication.class.getClassLoader().getResource(filename);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + filename);
        }
        return new File(resource.toURI());
    }

    // ------------------ Read JSON ------------------
    public static List<PortfolioTrade> readTradesFromJson(String fileName)
            throws IOException, URISyntaxException {

        File inputFile = resolveFileFromResources(fileName);
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        PortfolioTrade[] trades = om.readValue(inputFile, PortfolioTrade[].class);

        List<PortfolioTrade> tradeList = new ArrayList<>();
        for (PortfolioTrade trade : trades) {
            tradeList.add(trade);
        }
        return tradeList;
    }

    // ------------------ API Call ------------------
    public static List<TiingoCandle> getStockQuotes(String symbol, LocalDate from, LocalDate to)
            throws IOException {

        String url = String.format(
                "https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s&token=%s",
                symbol, from.toString(), to.toString(), TOKEN
        );

        RestTemplate restTemplate = new RestTemplate();

        TiingoCandle[] response = restTemplate.getForObject(url, TiingoCandle[].class);

        if (response == null) {
            return Collections.emptyList();
        }

        List<TiingoCandle> candleList = new ArrayList<>();
        for (TiingoCandle c : response) {
            candleList.add(c);
        }
        return candleList;
    }

    // ------------------ Wrapper ------------------
    public static List<TiingoCandle> fetchCandles(PortfolioTrade trade, LocalDate endDate)
            throws IOException {

        return getStockQuotes(
                trade.getSymbol(),
                trade.getPurchaseDate(),
                endDate
        );
    }

    // ------------------ Opening Price ------------------
    public static Double getOpeningPrice(List<TiingoCandle> candles) {
        return candles.get(0).getOpen();
    }

    // ------------------ Closing Price ------------------
    public static Double getClosingPrice(List<TiingoCandle> candles) {
        return candles.get(candles.size() - 1).getClose();
    }

    // ------------------ Annualized Return Formula ------------------
    public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
                                                              PortfolioTrade trade,
                                                              Double buyPrice,
                                                              Double sellPrice) {

        double totalReturns = (sellPrice - buyPrice) / buyPrice;

        long daysBetween = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
        double years = daysBetween / 365.24;

        double annualizedReturns = Math.pow(1 + totalReturns, 1 / years) - 1;

        return new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturns);
    }

    // ------------------ Compute all ------------------
    public static List<AnnualizedReturn> getAnnualizedReturn(List<PortfolioTrade> trades,
                                                             LocalDate endDate)
            throws IOException {

        List<AnnualizedReturn> returnsList = new ArrayList<>();

        for (PortfolioTrade trade : trades) {
            List<TiingoCandle> candles = fetchCandles(trade, endDate);

            Double buyPrice = getOpeningPrice(candles);
            Double sellPrice = getClosingPrice(candles);

            AnnualizedReturn ar = calculateAnnualizedReturns(endDate, trade, buyPrice, sellPrice);
            returnsList.add(ar);
        }

        returnsList.sort(
                Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed()
        );

        return returnsList;
    }

    // ------------------ MAIN ------------------
    public static void main(String[] args) throws Exception {

        List<PortfolioTrade> trades = readTradesFromJson("trades.json");
        LocalDate endDate = LocalDate.parse("2020-01-01");

        List<AnnualizedReturn> returnsList = getAnnualizedReturn(trades, endDate);

        returnsList.forEach(System.out::println);
    }
}
