package com.shine.hotels.io.model;

import java.io.Serializable;

public class Welcome implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -601226969038387256L;
    
    private String customerName;
    private String welcoming;
    private String languageName;
    private String languagePicNo;
	private String languagePicYes;
    private String languagevalue;
    private int status;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getWelcoming() {
		return welcoming;
	}
	public void setWelcoming(String welcoming) {
		welcoming = "        " + welcoming ;
		this.welcoming = welcoming;
	}
	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	public String getLanguagevalue() {
		return languagevalue;
	}
	public void setLanguagevalue(String languagevalue) {
		this.languagevalue = languagevalue;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
    public String getLanguagePicNo() {
		return languagePicNo;
	}
	public void setLanguagePicNo(String languagePicNo) {
		this.languagePicNo = languagePicNo;
	}
	public String getLanguagePicYes() {
		return languagePicYes;
	}
	public void setLanguagePicYes(String languagePicYes) {
		this.languagePicYes = languagePicYes;
	}
}
