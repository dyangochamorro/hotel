package com.shine.hotels.controller;

import com.shine.hotels.io.model.AppreciatemovieShow;
import com.shine.hotels.io.model.BootInfo;
import com.shine.hotels.io.model.HotelintroductionData;
import com.shine.hotels.io.model.HotelintroductionShow;
import com.shine.hotels.io.model.MusicData;
import com.shine.hotels.io.model.Subtitle;
import com.shine.hotels.io.model.ToolBarData;
import com.shine.hotels.io.model.WeatherList;
import com.shine.hotels.io.model.WorldTimeList;

public class Events {
    public static class GetWeatherEvent {
        public String res;
    }
    
    public static class GetResultEvent<T> {
        public T result;
    }
    
    public static class WelcomeEvent<T> {
        public T result;
    }
    
    public static class GetTvEvent<T> {
        public T result;
    }
    
    public static class BootInfoEvent {
        public BootInfo bootInfo;
    }
    
    public static class HotelintroductionIdEvent {
    	public int id ;
    }
    
    public static class HotelintroductionShowEvent {
    	public HotelintroductionShow result ;
    }

    public static class GetToolDataEvent {
        public ToolBarData data;
    }
    
    public static class AddType {
        public final static int WORLD_TIME = 1;
        public final static int WEATHER = 2;
        
        public int type;
    }
    
    public static class GetWorldTimes {
        public WorldTimeList data;
    }
    
    public static class GetWorldTimesRight {
    	public WorldTimeList data;
    }
    
    public static class GetAllWeather {
        public WeatherList data;
    }
    
    public static class AppreciatemovieIdEvent {
    	public int[] ids ;
    }
    
    public static class SubtitleEvent {
    	public Subtitle result ;
    }
    
    public static class AppreciatemovieShowEvent {
    	public AppreciatemovieShow result ;
    }
    
    public static class VODPayEvent {
        public AppreciatemovieShow result ;
        public int id;
        public int index;
    }
    
    public static class BackgroundMusicEvent {
        public MusicData data;
        public boolean isPlaying;
    }
    
    public static class SubHotelShowEvent {
        public HotelintroductionData data;
    }
    
    public static class PayBackEvent {
        public int id;
    }
}
