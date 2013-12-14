package com.shine.hotels.io.model;

import java.io.Serializable;

public class Menu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9090921172220468963L;

	String tag ;
	String menupic ;
	String menuname ;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getMenupic() {
		return menupic;
	}
	public void setMenupic(String menupic) {
		this.menupic = menupic;
	}
	public String getMenuname() {
		return menuname;
	}
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}


}
