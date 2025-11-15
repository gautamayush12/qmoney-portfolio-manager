package com.qmoney.client;

import com.qmoney.model.TiingoCandle;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

public class TiingoClient {

    private final String token = "8c02a65d8db7603a9b3e6cc9d54c8a2b58763b6c";
    public TiingoCandle[] getStockQuote(String symbol, LocalDate startDate, LocalDate endDate){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?" +
                "startDate=" + startDate + "&endDate=" + endDate + "&token=" + token;
        return restTemplate.getForObject(url, TiingoCandle[].class);
    }
}
