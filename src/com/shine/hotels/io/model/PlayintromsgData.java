package com.shine.hotels.io.model;

import java.io.Serializable;

public class PlayintromsgData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2374408790178125529L;

	String introtitle ;
	String introcontent ;
	int type;
	int id;
	
	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getIntrotitle() {
		return introtitle;
	}
	public void setIntrotitle(String introtitle) {
		this.introtitle = introtitle;
	}
	public String getIntrocontent() {
		return introcontent;
	}
	public void setIntrocontent(String introcontent) {
		this.introcontent = introcontent;
	}
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
