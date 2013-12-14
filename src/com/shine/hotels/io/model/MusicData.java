package com.shine.hotels.io.model;

import java.io.Serializable;

public class MusicData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2374408790178125529L;

	String musicname ;
	String musicurl ;
	
	public String getMusicname() {
		return musicname;
	}
	public void setMusicname(String musicname) {
		this.musicname = musicname;
	}
	public String getMusicurl() {
		return musicurl;
	}
	public void setMusicurl(String musicurl) {
		this.musicurl = musicurl;
	}
	
}
