package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class AppreciatetvList implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2066782463893105131L;
	
	private List<Appreciatetv> mList;
    
    public void setList(List<Appreciatetv> list) {
        mList = list;
    }
    
    public List<Appreciatetv> getLists() {
        return mList;
    }
}
