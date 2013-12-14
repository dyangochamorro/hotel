package com.shine.hotels.io.model;

import java.io.Serializable;

/**
 * 快速结账 
 * @author guoliang
 *
 */
public class Expresscheckout implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4565245322748981466L;
    
    private String expressinfo;

	public String getExpressinfo() {
		return expressinfo;
	}

	public void setExpressinfo(String expressinfo) {
		this.expressinfo = expressinfo;
	}
    
}
