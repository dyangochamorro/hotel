package com.shine.hotels.io.model;

import java.io.Serializable;

public class CategoryData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2374408790178125529L;

	String categoryname ;
	String categorypic ;
	String categoryurl ;
	
	public String getCategoryname() {
		return categoryname;
	}
	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
	public String getCategorypic() {
		return categorypic;
	}
	public void setCategorypic(String categorypic) {
		this.categorypic = categorypic;
	}
	public String getCategoryurl() {
		return categoryurl;
	}
	public void setCategoryurl(String categoryurl) {
		this.categoryurl = categoryurl;
	}
}
