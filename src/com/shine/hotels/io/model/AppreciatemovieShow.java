package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class AppreciatemovieShow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645914042462558678L;
	
	String movieposter ;
	String moviename ;
	String director ;
	String protagonist ;
	String runningtime ;
	String intro ;
	List<String> movieplayurl ;
	String moviepreviewurl ;
	List<PlayintromsgData> data ;
	
	public String getMovieposter() {
		return movieposter;
	}
	public void setMovieposter(String movieposter) {
		this.movieposter = movieposter;
	}
	public String getMoviename() {
		return moviename;
	}
	public void setMoviename(String moviename) {
		this.moviename = moviename;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getProtagonist() {
		return protagonist;
	}
	public void setProtagonist(String protagonist) {
		this.protagonist = protagonist;
	}
	public String getRunningtime() {
		return runningtime;
	}
	public void setRunningtime(String runningtime) {
		this.runningtime = runningtime;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getMoviepreviewurl() {
		return moviepreviewurl;
	}
	public void setMoviepreviewurl(String moviepreviewurl) {
		this.moviepreviewurl = moviepreviewurl;
	}
	public List<String> getMovieplayurl() {
		return movieplayurl;
	}
	public void setMovieplayurl(List<String> movieplayurl) {
		this.movieplayurl = movieplayurl;
	}
	public List<PlayintromsgData> getData() {
		return data;
	}
	public void setData(List<PlayintromsgData> data) {
		this.data = data;
	}

}
