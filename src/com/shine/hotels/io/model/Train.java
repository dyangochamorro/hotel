package com.shine.hotels.io.model;

import java.io.Serializable;

public class Train implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8294568629706774230L;
    
    private String number;
    private String type;
    private String originating;
    private String departure;
    private String departureTime;
    private String terminal;
    private String terminalTime;
    private String period;
    private String last;
    private String mileage;
    
    public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getOriginating() {
        return originating;
    }
    public void setOriginating(String originating) {
        this.originating = originating;
    }
    public String getDeparture() {
        return departure;
    }
    public void setDeparture(String departure) {
        this.departure = departure;
    }
    public String getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    public String getTerminal() {
        return terminal;
    }
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
    public String getTerminalTime() {
        return terminalTime;
    }
    public void setTerminalTime(String terminalTime) {
        this.terminalTime = terminalTime;
    }
    public String getPeriod() {
        return period;
    }
    public void setPeriod(String period) {
        this.period = period;
    }
    
    
}
