package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class GuestBookList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3014790764118848245L;
    
    private List<GuestBook> mList;
    
    public void setList(List<GuestBook> list) {
        mList = list;
    }
    
    public List<GuestBook> getLists() {
        return mList;
    }
}
