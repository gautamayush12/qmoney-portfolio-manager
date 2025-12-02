package com.qmoney.service;

import com.qmoney.model.TiingoCandle;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TiingoService implements StockQuotesService {

    private final String token;

    public TiingoService(String token) {
        this.token = token;
    }

    @Override
    public List<TiingoCandle> getStockQuote(String symbol,
                                            LocalDate from,
                                            LocalDate to) throws IOException {

        String url = "https://api.tiingo.com/tiingo/daily/" + symbol
                + "/prices?startDate=" + from
                + "&endDate=" + to
                + "&token=" + token;

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
}
