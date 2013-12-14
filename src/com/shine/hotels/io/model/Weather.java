
package com.shine.hotels.io.model;

import java.io.Serializable;

public class Weather implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5726032084751433719L;

    private String code;
    private String citycode;
    private int windzh;
    private int winden;
    private String todaycode;
    private int todaylow;
    private int todayhigh;
    private int tomorrowcode;
    private int tomorrowlow;
    private int tomorrowhigh;
    private int aftertomorrowcode;
    private int aftertomorrowlow;
    private int aftertomorrowhith;
    private String updatetime;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getWindzh() {
		return windzh;
	}
	public void setWindzh(int windzh) {
		this.windzh = windzh;
	}
	public int getWinden() {
		return winden;
	}
	public void setWinden(int winden) {
		this.winden = winden;
	}
	public String getTodaycode() {
		return todaycode;
	}
	public void setTodaycode(String todaycode) {
		this.todaycode = todaycode;
	}
	public int getTodaylow() {
		return todaylow;
	}
	public void setTodaylow(int todaylow) {
		this.todaylow = todaylow;
	}
	public int getTodayhigh() {
		return todayhigh;
	}
	public void setTodayhigh(int todayhigh) {
		this.todayhigh = todayhigh;
	}
	public int getTomorrowcode() {
		return tomorrowcode;
	}
	public void setTomorrowcode(int tomorrowcode) {
		this.tomorrowcode = tomorrowcode;
	}
	public int getTomorrowlow() {
		return tomorrowlow;
	}
	public void setTomorrowlow(int tomorrowlow) {
		this.tomorrowlow = tomorrowlow;
	}
	public int getTomorrowhigh() {
		return tomorrowhigh;
	}
	public void setTomorrowhigh(int tomorrowhigh) {
		this.tomorrowhigh = tomorrowhigh;
	}
	public int getAftertomorrowcode() {
		return aftertomorrowcode;
	}
	public void setAftertomorrowcode(int aftertomorrowcode) {
		this.aftertomorrowcode = aftertomorrowcode;
	}
	public int getAftertomorrowlow() {
		return aftertomorrowlow;
	}
	public void setAftertomorrowlow(int aftertomorrowlow) {
		this.aftertomorrowlow = aftertomorrowlow;
	}
	public int getAftertomorrowhith() {
		return aftertomorrowhith;
	}
	public void setAftertomorrowhith(int aftertomorrowhith) {
		this.aftertomorrowhith = aftertomorrowhith;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

}
