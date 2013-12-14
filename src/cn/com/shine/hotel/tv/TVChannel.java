package cn.com.shine.hotel.tv;

public class TVChannel {

	private int channelnum;
	private String channelcode;
	private int channelfrequey;
	public TVChannel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TVChannel(int channelnum, String channelcode, int channelfrequey) {
		super();
		this.channelnum = channelnum;
		this.channelcode = channelcode;
		this.channelfrequey = channelfrequey;
	}
	public int getChannelnum() {
		return channelnum;
	}
	public void setChannelnum(int channelnum) {
		this.channelnum = channelnum;
	}
	public String getChannelcode() {
		return channelcode;
	}
	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}
	public int getChannelfrequey() {
		return channelfrequey;
	}
	public void setChannelfrequey(int channelfrequey) {
		this.channelfrequey = channelfrequey;
	}
	
	
}
