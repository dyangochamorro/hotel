package com.shine.hotels.io.model;

import java.io.Serializable;

public class GuestBook implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8394618383318387037L;

    private String message;
    private String receiver;
    private String sender;
    private String time;
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
