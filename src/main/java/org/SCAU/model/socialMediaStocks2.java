package org.SCAU.model;

import java.util.Date;
import java.util.Objects;

public class socialMediaStocks2 {
    public String date;
    public String symbol;
    public String adjClose;
    public String close;
    public String high;
    public String low;
    public String open;
    public String volume;

    public socialMediaStocks2() {
    }

    public socialMediaStocks2(String date, String symbol, String adjClose, String close, String high, String low, String open, String volume) {
        this.date = date;
        this.symbol = symbol;
        this.adjClose = adjClose;
        this.close = close;
        this.high = high;
        this.low = low;
        this.open = open;
        this.volume = volume;
    }

    public String getDate() {
        return date;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAdjClose() {
        return adjClose;
    }

    public String getClose() {
        return close;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getOpen() {
        return open;
    }

    public String getVolume() {
        return volume;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setAdjClose(String adjClose) {
        this.adjClose = adjClose;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        socialMediaStocks2 that = (socialMediaStocks2) o;
        return Objects.equals(date, that.date) && Objects.equals(symbol, that.symbol) && Objects.equals(adjClose, that.adjClose) && Objects.equals(close, that.close) && Objects.equals(high, that.high) && Objects.equals(low, that.low) && Objects.equals(open, that.open) && Objects.equals(volume, that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, symbol, adjClose, close, high, low, open, volume);
    }

    @Override
    public String toString() {
        return "socialMediaStocks2{" +
                "date='" + date + '\'' +
                ", symbol='" + symbol + '\'' +
                ", adjClose='" + adjClose + '\'' +
                ", close='" + close + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", open='" + open + '\'' +
                ", volume='" + volume + '\'' +
                '}';
    }

}
