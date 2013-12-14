package com.shine.hotels.io.model;

import java.io.Serializable;

public class Bill implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4565245322748981466L;
    
    private int index;
    private String ting;
    private int amount;
    private int price;
    
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public String getTing() {
        return ting;
    }
    public void setTing(String ting) {
        this.ting = ting;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    
}
