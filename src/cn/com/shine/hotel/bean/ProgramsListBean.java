package cn.com.shine.hotel.bean;

public class ProgramsListBean {
	private String programname;
	private String programid;
	
	
	
	public ProgramsListBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProgramsListBean(String programname,
			String programid) {
		super();
		this.programname = programname;
		this.programid = programid;
	}
	public String getProgramname() {
		return programname;
	}
	public void setProgramname(String programname) {
		this.programname = programname;
	}
	
	public String getProgramid() {
		return programid;
	}
	public void setProgramid(String programid) {
		this.programid = programid;
	}
	
	
	
}
