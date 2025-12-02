package com.qmoney;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.qmoney.model.PortfolioTrade;
import com.qmoney.model.AnnualizedReturn;
import com.qmoney.service.PortfolioManager;
import com.qmoney.service.PortfolioManagerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PortfolioManagerApplication {

    public static File resolveFileFromResources(String filename) {
        try {
            URL resource = PortfolioManagerApplication.class.getClassLoader().getResource(filename);
            return new File(resource.toURI());
        } catch (Exception e) {
            throw new RuntimeException("File not found: " + filename);
        }
    }

    public static List<PortfolioTrade> readTradesFromJson(String fileName) throws IOException {
        File file = resolveFileFromResources(fileName);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        PortfolioTrade[] trades = mapper.readValue(file, PortfolioTrade[].class);

        List<PortfolioTrade> list = new ArrayList<>();
        for (PortfolioTrade t : trades) list.add(t);
        return list;
    }

    public static void main(String[] args) throws Exception {

        List<PortfolioTrade> trades = readTradesFromJson("trades.json");
        LocalDate endDate = LocalDate.parse("2020-01-05");

        PortfolioManager manager =
                PortfolioManagerFactory.getPortfolioManager("YOUR_TOKEN_HERE");

        List<AnnualizedReturn> result =
                manager.calculateAnnualizedReturn(trades, endDate);

        for (AnnualizedReturn ar : result) {
            System.out.println(ar);
        }
    }
}
