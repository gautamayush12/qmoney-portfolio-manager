package com.qmoney.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class TiingoCandle {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    // Getters and setters
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }
    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }
    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }
    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }
    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }
    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "TiingoCandle{" +
                "date=" + date +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}
