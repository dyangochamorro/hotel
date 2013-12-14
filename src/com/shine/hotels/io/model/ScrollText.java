package com.shine.hotels.io.model;

import java.io.Serializable;

public class ScrollText implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -882875723028196293L;
    
    String content;
    String startTime;
    String endTime;
    String background;
    String color;
    String size;
    String coordinate;
    String tempo;
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getBackground() {
        return background;
    }
    public void setBackground(String background) {
        this.background = background;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }
    public String getTempo() {
        return tempo;
    }
    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

}
