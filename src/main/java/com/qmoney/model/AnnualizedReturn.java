package com.qmoney.model;

public class AnnualizedReturn {

    private String symbol;
    private Double annualizedReturn;
    private Double totalReturn;

    public AnnualizedReturn(String symbol, Double annualizedReturn, Double totalReturn) {
        this.symbol = symbol;
        this.annualizedReturn = annualizedReturn;
        this.totalReturn = totalReturn;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getAnnualizedReturn() {
        return annualizedReturn;
    }

    public Double getTotalReturn() {
        return totalReturn;
    }

    @Override
    public String toString() {
        return "AnnualizedReturn{" +
                "symbol='" + symbol + '\'' +
                ", annualizedReturn=" + annualizedReturn +
                ", totalReturn=" + totalReturn +
                '}';
    }
}
