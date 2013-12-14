package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class HotelintroductionShow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9099557036306404849L;
	
	String title ;
	String content ;
	String tvUrl ;
	List<String> pics ;
	int flag;
	String pic;
	
	public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTvUrl() {
		return tvUrl;
	}
	public void setTvUrl(String tvUrl) {
		this.tvUrl = tvUrl;
	}
	public List<String> getPics() {
		return pics;
	}
	public void setPics(List<String> pics) {
		this.pics = pics;
	}
    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }


}
