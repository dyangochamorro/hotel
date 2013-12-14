package com.shine.hotels.io.model;

import java.io.Serializable;

public class HotelintroductionShowPic implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2771722801543717740L;
	
	String pic ;
	int flag ;
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	

}
