package com.qmoney;

import com.qmoney.model.AnnualizedReturn;
import com.qmoney.model.PortfolioTrade;
import com.qmoney.model.TiingoCandle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class PortfolioManagerApplicationTest {

    @Test
    public void testReadTradesFromJson() throws Exception {
        List<PortfolioTrade> trades =
                PortfolioManagerApplication.readTradesFromJson("trades.json");

        Assertions.assertFalse(trades.isEmpty());
        Assertions.assertEquals("MSFT", trades.get(0).getSymbol());
    }

    @Test
    public void testFetchCandles() throws Exception {
        PortfolioTrade trade = new PortfolioTrade("AAPL", LocalDate.parse("2020-01-01"), 10);

        List<TiingoCandle> candles =
                PortfolioManagerApplication.fetchCandles(trade, LocalDate.parse("2020-01-05"));

        Assertions.assertFalse(candles.isEmpty());
    }

    @Test
    public void testAnnualizedReturnCalculation() {
        PortfolioTrade trade = new PortfolioTrade("AAPL", LocalDate.parse("2019-01-01"), 10);
        double buy = 100.0;
        double sell = 120.0;

        AnnualizedReturn ar = PortfolioManagerApplication
                .calculateAnnualizedReturns(LocalDate.parse("2020-01-01"), trade, buy, sell);

        Assertions.assertTrue(ar.getAnnualizedReturn() > 0);
    }
}
