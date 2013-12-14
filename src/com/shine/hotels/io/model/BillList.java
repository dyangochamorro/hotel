package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class BillList implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2227065395280158143L;
    
    private List<Bill> mList;
    
    public void setList(List<Bill> list) {
        mList = list;
    }
    
    public List<Bill> getLists() {
        return mList;
    }
}
