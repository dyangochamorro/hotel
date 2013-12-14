package com.shine.hotels.io.model;

import java.io.Serializable;

public class CarRental implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5820694671327731753L;
    
    private String car;
    private int seatsNum;
    private int rent;
    private String driver;
    
    public String getCar() {
        return car;
    }
    public void setCar(String car) {
        this.car = car;
    }
    public int getSeatsNum() {
        return seatsNum;
    }
    public void setSeatsNum(int seatsNum) {
        this.seatsNum = seatsNum;
    }
    public int getRent() {
        return rent;
    }
    public void setRent(int rent) {
        this.rent = rent;
    }
    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }
}
