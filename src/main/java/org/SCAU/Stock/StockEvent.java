package org.SCAU.Stock;

import java.util.Objects;

public class StockEvent {
    public StockEvent() {
    }

    public String name;
    public Float price;
    public Long timeStamp;

    public StockEvent(String name, String price, String timeStamp) {
        this.name = name;
        this.price = Float.valueOf(price);
        this.timeStamp = Float.valueOf(timeStamp).longValue();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "StockEvent{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", timeStamp=" + timeStamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockEvent that = (StockEvent) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(timeStamp, that.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, timeStamp);
    }
}
