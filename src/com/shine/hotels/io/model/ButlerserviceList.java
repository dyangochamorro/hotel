package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class ButlerserviceList implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7364600978693936264L;
    
    private List<Butlerservice> mList;
    
    public void setList(List<Butlerservice> list) {
        mList = list;
    }
    
    public List<Butlerservice> getLists() {
        return mList;
    }
}
