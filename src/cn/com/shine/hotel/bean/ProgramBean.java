package cn.com.shine.hotel.bean;

import java.io.Serializable;

public class ProgramBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1329798771052193641L;
	private String channel_name;
	private String channel_frequee;
	
	
	public ProgramBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProgramBean(String channel_name, String channel_frequee) {
		super();
		this.channel_name = channel_name;
		this.channel_frequee = channel_frequee;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public String getChannel_frequee() {
		return channel_frequee;
	}
	public void setChannel_frequee(String channel_frequee) {
		this.channel_frequee = channel_frequee;
	}
	
	

}
