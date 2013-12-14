package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class UsefulTelList implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7364600978693936264L;
    
    private List<UsefulTel> mList;
    
    public void setList(List<UsefulTel> list) {
        mList = list;
    }
    
    public List<UsefulTel> getLists() {
        return mList;
    }
}
