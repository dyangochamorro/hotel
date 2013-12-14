
package com.shine.hotels.io.model;

import java.io.Serializable;

public class WorldTime implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -7629901959993855126L;

    private String code;
    private String city;
    private String timezone;

    public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
