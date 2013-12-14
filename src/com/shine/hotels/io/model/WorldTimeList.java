package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class WorldTimeList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5569632177290224081L;

    private List<WorldTime> mWorldTimes;
    
    public void setWorldTimes(List<WorldTime> list) {
        mWorldTimes = list;
    }
    
    public List<WorldTime> getWorldTimes() {
        return mWorldTimes;
    }
}
