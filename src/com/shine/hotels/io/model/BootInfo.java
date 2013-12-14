package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class BootInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2424636735218629939L;
    
    private int type;
    private List<String> urls;
    private int num;
    private String time;
    private String logo;
    
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<String> getUrls() {
        return urls;
    }
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    
}
