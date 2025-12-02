package com.qmoney.service;

import com.qmoney.model.TiingoCandle;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface StockQuotesService {

    List<TiingoCandle> getStockQuote(String symbol,
                                     LocalDate from,
                                     LocalDate to) throws IOException;
}
