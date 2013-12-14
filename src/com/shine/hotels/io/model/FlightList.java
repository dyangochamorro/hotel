package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class FlightList implements Serializable {

    
    /**
     * 
     */
    private static final long serialVersionUID = 8676322548127909181L;
    private List<Flight> mFlights;
    
    private String category;
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFlights(List<Flight> list) {
        mFlights = list;
    }
    
    public List<Flight> getFlights() {
        return mFlights;
    }

}
