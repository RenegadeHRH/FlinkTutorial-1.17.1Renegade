package org.SCAU.model;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class StockEventNew {
    public String index;
    public String ticker;
    public Date date;
    public float open;
    public float high;
    public float low;
    public float close;
    public int Volume;

    public StockEventNew() {
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getVolume() {
        return Volume;
    }

    public void setVolume(int volume) {
        Volume = volume;
    }


    public StockEventNew(String index, String ticker, String date, String open, String high, String low, String close, String volume) throws ParseException {

        this.index = index;
        this.ticker = ticker;
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        this.date = ft.parse(date);
        this.open = Float.parseFloat(open);
        this.high = Float.parseFloat(high);
        this.low = Float.parseFloat(low);
        this.close = Float.parseFloat(close);
        this.Volume = Integer.parseInt(volume);
    }
    public float getPriceChange() {
        return (this.close - this.open) / this.open;
    }


    @Override
    public String toString() {
        return "StockEventNew{" +
                "index='" + index + '\'' +
                ", ticker='" + ticker + '\'' +
                ", date=" + date +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", Volume=" + Volume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockEventNew that = (StockEventNew) o;
        return Float.compare(open, that.open) == 0 && Float.compare(high, that.high) == 0 && Float.compare(low, that.low) == 0 && Float.compare(close, that.close) == 0 && Volume == that.Volume && Objects.equals(index, that.index) && Objects.equals(ticker, that.ticker) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, ticker, date, open, high, low, close, Volume);
    }
}
