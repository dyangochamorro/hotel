package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class Appreciatemovie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645914042462558678L;
	
	String movietypename ;
	List<MovieData> data ;
	
	public String getMovietypename() {
		return movietypename;
	}
	public void setMovietypename(String movietypename) {
		this.movietypename = movietypename;
	}
	public List<MovieData> getData() {
		return data;
	}
	public void setData(List<MovieData> data) {
		this.data = data;
	}

}
