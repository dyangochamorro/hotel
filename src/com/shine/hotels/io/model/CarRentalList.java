package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class CarRentalList implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2227065395280158143L;
    
    private List<CarRental> mList;
    
    public void setList(List<CarRental> list) {
        mList = list;
    }
    
    public List<CarRental> getLists() {
        return mList;
    }
}
