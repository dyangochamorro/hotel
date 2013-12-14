package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class MemoservList implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -2227065395280158143L;
    
    private List<Memoserv> mList;
    
    public void setList(List<Memoserv> list) {
        mList = list;
    }
    
    public List<Memoserv> getLists() {
        return mList;
    }
}
