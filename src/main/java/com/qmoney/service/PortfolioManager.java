package com.qmoney.service;

import com.qmoney.model.AnnualizedReturn;
import com.qmoney.model.PortfolioTrade;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface PortfolioManager {

    List<AnnualizedReturn> calculateAnnualizedReturn(
            List<PortfolioTrade> trades,
            LocalDate endDate) throws IOException;
}
