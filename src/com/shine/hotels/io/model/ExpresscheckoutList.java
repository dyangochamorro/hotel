package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class ExpresscheckoutList implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2227065395280158143L;
    
    private List<Expresscheckout> mList;
    
    public void setList(List<Expresscheckout> list) {
        mList = list;
    }
    
    public List<Expresscheckout> getLists() {
        return mList;
    }
}
