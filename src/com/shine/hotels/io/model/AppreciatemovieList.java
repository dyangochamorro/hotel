package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class AppreciatemovieList implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2066782463893105131L;
	
	private List<Appreciatemovie> mList;
    
    public void setList(List<Appreciatemovie> list) {
        mList = list;
    }
    
    public List<Appreciatemovie> getLists() {
        return mList;
    }
}
