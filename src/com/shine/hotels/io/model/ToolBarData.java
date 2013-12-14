package com.shine.hotels.io.model;

import java.io.Serializable;

public class ToolBarData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4265181064261286579L;
	
	private String city;
	private String temperature;
	private int msgNum;
	private String weather;
	private String logo;
	
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public int getMsgNum() {
		return msgNum;
	}
	public void setMsgNum(int msgNum) {
		this.msgNum = msgNum;
	}
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }

}
