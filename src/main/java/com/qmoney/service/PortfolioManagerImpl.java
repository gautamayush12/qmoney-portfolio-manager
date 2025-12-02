package com.qmoney.service;

import com.qmoney.model.AnnualizedReturn;
import com.qmoney.model.PortfolioTrade;
import com.qmoney.model.TiingoCandle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PortfolioManagerImpl implements PortfolioManager {

    private final StockQuotesService quotesService;

    public PortfolioManagerImpl(StockQuotesService quotesService) {
        this.quotesService = quotesService;
    }

    @Override
    public List<AnnualizedReturn> calculateAnnualizedReturn(
            List<PortfolioTrade> trades,
            LocalDate endDate) throws IOException {

        List<AnnualizedReturn> result = new ArrayList<>();

        for (PortfolioTrade trade : trades) {

            List<TiingoCandle> candles =
                    quotesService.getStockQuote(trade.getSymbol(),
                            trade.getPurchaseDate(),
                            endDate);

            if (candles.isEmpty()) continue;

            double buyPrice = candles.get(0).getOpen();
            double sellPrice = candles.get(candles.size() - 1).getClose();

            double totalReturn = (sellPrice - buyPrice) / buyPrice;

            long days = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
            double years = days / 365.24;

            double annualized = Math.pow(1 + totalReturn, 1 / years) - 1;

            result.add(new AnnualizedReturn(trade.getSymbol(), annualized, totalReturn));
        }

        // Sort descending
        Collections.sort(result, new Comparator<AnnualizedReturn>() {
            @Override
            public int compare(AnnualizedReturn a1, AnnualizedReturn a2) {
                return Double.compare(a2.getAnnualizedReturn(), a1.getAnnualizedReturn());
            }
        });

        return result;
    }
}
