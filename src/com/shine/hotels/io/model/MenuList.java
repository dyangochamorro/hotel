package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class MenuList implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2227065395280158143L;
    
    private List<Menu> mList;
    
    public void setList(List<Menu> list) {
        mList = list;
    }
    
    public List<Menu> getLists() {
        return mList;
    }
}
