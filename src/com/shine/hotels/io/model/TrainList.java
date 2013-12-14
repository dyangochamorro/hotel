package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class TrainList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8442353297427576535L;
    
    private List<Train> mTrains;
    
    public void setTrains(List<Train> list) {
        mTrains = list;
    }
    
    public List<Train> getTrains() {
        return mTrains;
    }

}
