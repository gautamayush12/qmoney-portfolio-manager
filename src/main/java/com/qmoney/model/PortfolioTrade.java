package com.qmoney.model;

import java.time.LocalDate;

public class PortfolioTrade {

    private String symbol;
    private LocalDate purchaseDate;
    private int quantity;

    public PortfolioTrade(){

    }

    public PortfolioTrade(String symbol, LocalDate purchaseDate, int quantity){
        this.symbol = symbol;
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "Symbol:" + " " + symbol + " Purchased on:" + " " + purchaseDate + " &" + " Quantity is: " + quantity;
    }
}
