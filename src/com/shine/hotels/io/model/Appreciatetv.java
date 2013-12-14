package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class Appreciatetv implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645914042462558678L;
	
	String categorytypename ;
	List<CategoryData> data ;
	
	public String getCategorytypename() {
		return categorytypename;
	}
	public void setCategorytypename(String categorytypename) {
		this.categorytypename = categorytypename;
	}
	public List<CategoryData> getData() {
		return data;
	}
	public void setData(List<CategoryData> data) {
		this.data = data;
	}

}
