package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class WeatherList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1079745373090629446L;
    
    private List<Weather> mWeathers;
    
    public void setWeathers(List<Weather> list) {
        mWeathers = list;
    }
    
    public List<Weather> getWeathers() {
        return mWeathers;
    }

}
