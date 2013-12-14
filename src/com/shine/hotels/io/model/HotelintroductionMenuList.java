package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class HotelintroductionMenuList implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2066782463893105131L;
    /**
	 * 
	 */
	
	private List<HotelintroductionMenu> mList;
    
    public void setList(List<HotelintroductionMenu> list) {
        mList = list;
    }
    
    public List<HotelintroductionMenu> getLists() {
        return mList;
    }
}
