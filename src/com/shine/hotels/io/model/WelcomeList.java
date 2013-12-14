package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class WelcomeList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1079745373090629446L;
    
    private List<Welcome> mWelcomes;

	public List<Welcome> getmWelcomes() {
		return mWelcomes;
	}

	public void setmWelcomes(List<Welcome> mWelcomes) {
		this.mWelcomes = mWelcomes;
	}

}
