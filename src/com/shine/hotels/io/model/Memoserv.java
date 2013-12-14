package com.shine.hotels.io.model;

import java.io.Serializable;

/**
 * 留言服务
 * @author guoliang
 *
 */
public class Memoserv implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4565245322748981466L;
    
    private int index;
    private String content;
	private String date ;
	
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
    
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    
}
