package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class HotelintroductionMenu implements Serializable {
    public final static int TYPE_LIST = 1;
    public final static int TYPE_GRID = 2;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645914042462558678L;
	
	String sortingname ;
	String sortingpic ;
	int type ;
	List<HotelintroductionData> data ;
	
	public String getSortingname() {
		return sortingname;
	}
	public void setSortingname(String sortingname) {
		this.sortingname = sortingname;
	}
	public String getSortingpic() {
		return sortingpic;
	}
	public void setSortingpic(String sortingpic) {
		this.sortingpic = sortingpic;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<HotelintroductionData> getData() {
		return data;
	}
	public void setData(List<HotelintroductionData> data) {
		this.data = data;
	}

}
