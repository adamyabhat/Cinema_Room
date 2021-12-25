package cinema.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Seat {
    private int row;
    private int column;
    private boolean isBooked;
    private int price;
    private UUID token;
    public Seat(int row,int column, int price) {
        this.row = row;
        this.column=column;
        this.price=price;
        this.isBooked=false;
        this.token = UUID.randomUUID();
    }

    @JsonIgnore
    public boolean isBooked() {
        return isBooked;
    }

    @JsonProperty("")
    public int getPrice() {
        return price;
    }

    @JsonProperty("")
    public int getRow() {
        return row;
    }

    @JsonIgnore
    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    @JsonProperty("")
    public void setPrice(int price) {
        this.price = price;
    }

    @JsonProperty("")
    public int getColumn() {
        return column;
    }

    @JsonProperty("")
    public void setRow(int row) {
        this.row = row;
    }

    @JsonProperty("")
    public void setColumn(int column) {
        this.column = column;
    }

}
