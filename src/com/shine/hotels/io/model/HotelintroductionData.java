package com.shine.hotels.io.model;

import java.io.Serializable;

public class HotelintroductionData implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2771722801543717740L;
	
	String dataContent ;
	int dataId ;
	
	public String getDataContent() {
		return dataContent;
	}
	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}
	

}
