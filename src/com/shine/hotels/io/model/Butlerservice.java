package com.shine.hotels.io.model;

import java.io.Serializable;

public class Butlerservice implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7087753564848221173L;
	
	public static final int TYPE_INFO = 1;
    public static final int TYPE_SERVICE = 2;

    
    private String owner;
    
    /**
     * 内容类型
     */
    private int type;
    
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    
}
