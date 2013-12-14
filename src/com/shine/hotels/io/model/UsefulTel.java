package com.shine.hotels.io.model;

import java.io.Serializable;

public class UsefulTel implements Serializable {
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_HOTEL = 2;

    /**
     * 
     */
    private static final long serialVersionUID = 3165543231831636787L;
    
    private String owner;
    private String number;
    
    /**
     * 号码类型
     */
    private int type;
    
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    
}
