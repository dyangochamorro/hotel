package com.shine.hotels.io.model;

import java.io.Serializable;

public class MovieData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2374408790178125529L;

	String moviename ;
	String moviepic ;
//	int movieId ;
	int movietype;
	int[] movieIds;
	
	public int[] getMovieIds() {
        return movieIds;
    }
    public void setMovieIds(int[] movieIds) {
        this.movieIds = movieIds;
    }
    public int getMovietype() {
        return movietype;
    }
    public void setMovietype(int movietype) {
        this.movietype = movietype;
    }
    public String getMoviename() {
		return moviename;
	}
	public void setMoviename(String moviename) {
		this.moviename = moviename;
	}
	public String getMoviepic() {
		return moviepic;
	}
	public void setMoviepic(String moviepic) {
		this.moviepic = moviepic;
	}
//	public int getMovieId() {
//		return movieId;
//	}
//	public void setMovieId(int movieId) {
//		this.movieId = movieId;
//	}
}
