package com.shine.hotels.io.model;

import java.io.Serializable;
import java.util.List;

public class Appreciatemusic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645914042462558678L;
	
	String musictypepic ;
	String specialposter ;
	String musicspecialname ;
	public String getMusicspecialname() {
		return musicspecialname;
	}
	public void setMusicspecialname(String musicspecialname) {
		this.musicspecialname = musicspecialname;
	}
	List<MusicData> data ;
	
	public String getMusictypepic() {
		return musictypepic;
	}
	public void setMusictypepic(String musictypepic) {
		this.musictypepic = musictypepic;
	}
	public String getSpecialposter() {
		return specialposter;
	}
	public void setSpecialposter(String specialposter) {
		this.specialposter = specialposter;
	}
	public List<MusicData> getData() {
		return data;
	}
	public void setData(List<MusicData> data) {
		this.data = data;
	}

}
