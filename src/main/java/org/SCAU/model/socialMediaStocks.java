package org.SCAU.model;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class socialMediaStocks {
    public Date date;
    public String symbol;
    public float adjClose;
    public float close;
    public float high;
    public float low;
    public float open;
    public int volume;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(float adjClose) {
        this.adjClose = adjClose;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
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

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
    public socialMediaStocks(){

    }
    public socialMediaStocks(String date, String symbol, String adjClose, String close, String high, String low, String open, String volume) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        this.date = ft.parse(date);
        this.symbol = symbol;
        this.adjClose = Float.parseFloat(adjClose);
        this.close = Float.parseFloat(close);
        this.high = Float.parseFloat(high);
        this.low = Float.parseFloat(low);
        this.open = Float.parseFloat(open);
        this.volume =(int) Float.parseFloat(volume);
    }

    @Override
    public String toString() {
        return "socialMediaStocks{" +
                "date=" + date +
                ", symbol='" + symbol + '\'' +
                ", adjClose=" + adjClose +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                ", volume=" + volume +
                '}';
    }
    public float getPriceChange(){
        return (this.close - this.open) / this.open;
    }
}
