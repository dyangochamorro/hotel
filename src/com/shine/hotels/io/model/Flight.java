package com.shine.hotels.io.model;

import java.io.Serializable;

public class Flight implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -696861587721179075L;
    
    private String number;
    private String airline;
    private String expectedDepart;
    private String actualDepart;
    private String expectedArrive;
    private String actualArrive;
    private String departure;
    private String destination;
    private String departureTerminal;
    private String destinationTerminal;
    private String status;
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getAirline() {
        return airline;
    }
    public void setAirline(String airline) {
        this.airline = airline;
    }
    public String getExpectedDepart() {
        return expectedDepart;
    }
    public void setExpectedDepart(String expectedDepart) {
        this.expectedDepart = expectedDepart;
    }
    public String getActualDepart() {
        return actualDepart;
    }
    public void setActualDepart(String actualDepart) {
        this.actualDepart = actualDepart;
    }
    public String getExpectedArrive() {
        return expectedArrive;
    }
    public void setExpectedArrive(String expectedArrive) {
        this.expectedArrive = expectedArrive;
    }
    public String getActualArrive() {
        return actualArrive;
    }
    public void setActualArrive(String actualArrive) {
        this.actualArrive = actualArrive;
    }
    public String getDeparture() {
        return departure;
    }
    public void setDeparture(String departure) {
        this.departure = departure;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDepartureTerminal() {
        return departureTerminal;
    }
    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }
    public String getDestinationTerminal() {
        return destinationTerminal;
    }
    public void setDestinationTerminal(String destinationTerminal) {
        this.destinationTerminal = destinationTerminal;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
