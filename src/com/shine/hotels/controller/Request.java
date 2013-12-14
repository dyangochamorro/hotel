package com.shine.hotels.controller;

import com.android.internal.telephony.MccTable;
import com.shine.hotels.Config;
import com.shine.hotels.HotelsApplication;

import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * 封装view需要进行异步操作的请求
 *
 */
public class Request {
    /*
     * request的状态
     */
    public static final byte STATE_NULL = 0x00;
    public static final byte STATE_PENDING = 0x01;
    public static final byte STATE_RUNNING = 0x02;
    public static final byte STATE_CANCELED = 0x04;
    public static final byte STATE_FINISHED = 0x08;
    
    private final static String KEY_LANG = "lang";
    
    /*
     * 请求id，标识请求类型
     */
    private String mAction;
    /*
     * 请求所带的参数
     */
    private Bundle mParam;
    /*
     * 
     */
    private Object mObj;
    /*
     * 请求的状态
     */
    /*package*/ volatile byte state = STATE_NULL;
    
    
    private Request() {
        mParam = new Bundle();
    }
    
    public String getAction() {
        return mAction;
    }
    
    public Bundle getParam() {
        return mParam;
    }
    
    public Object getObject() {
        return mObj;
    }
    
    /**
     * 构造Request的工具类
     *
     */
    public static class Builder {
        private Request mRequest;
        
        /**
         * 首先获取一个request对象
         * @param id
         * @return
         */
        // TODO get one from object pool
        public Builder obtain(String action) {
            mRequest = new Request();
            mRequest.mAction = action;
            
            // TODO
            mRequest.mParam.putString(KEY_LANG, HotelsApplication.getCurrentLang());
            
            return this;
        }
        
        /**
         * 添加字符串型的参数
         * @param key
         * @param value
         * @return
         */
        public Builder putStringParam(String key, String value) {
            assert mRequest != null;
            
            mRequest.mParam.putString(key, value);
            return this;
        }
        
        public Builder putObject(Object obj) {
            assert mRequest != null;
            
            mRequest.mObj = obj;
            
            return this;
        }
        
        /**
         * 获得构造好的request对象
         * @return
         */
        public Request getResult() {
            return mRequest;
        }
    }
    
    public static class Action {
        /*
         * 时钟
         */
        public static final String WATCH = "hotels.system.WATCH";
        /*
         * 滚动字幕
         */
        public static final String MARQUEE = "hotels.system.MARQUEE";
        /*
         * 滚动字幕
         */
        public static final String PLAY_BACKGROUND_MUSIC = "hotels.system.PLAYBACKGROUNDMUSIC";
        /*
         * 获取旅游指南
         */
        public static final String GET_GUIDE = "hotels.travelinfo.GETGUIDE";
        /*
         * 获取多个城市天气预报
         */
        public static final String GET_WEATHERS = "hotels.travelinfo.GETWEATHER";
        /*
         * 天气预报返回值
         */
        public static final String GET_WEATHERS_BACK = "hotels.travelinfo.GETWEATHER.BACK";
        /*
         * 获取世界时间
         */
        public static final String GET_WORLDTIME = "hotels.travelinfo.GETWORLDTIME";
        /*
         * 世界时间返回值
         */
        public static final String GET_WORLDTIME_BACK = "hotels.travelinfo.GETWORLDTIME.BACK";
        /*
         * 获取列车信息
         */
        public static final String GET_SCHEDULE = "hotels.travelinfo.GETSCHEDULE";
        /*
         * 获取列车信息
         */
        public static final String GET_FLIGHTINFO = "hotels.travelinfo.GETFLIGHTINFO";
        /*
         * 获取租车信息
         */
        public static final String GET_CAR = "hotels.travelinfo.GETCAR";
        /*
         * 帐单查询
         */
        public static final String GET_BILLING = "hotels.roomservice.GETBILLING";
        /*
         * 获取电话
         */
        public static final String GET_PHONE_NO = "hotels.roomservice.GETPHONENO";
        /*
         * 快速结账 
         */
        public static final String GET_EXPRESSCHECKOUT = "hotels.roomservice.GETEXPRESSCHECKOUT" ;
        /*
         * 快速结账确认 
         */
        public static final String GET_EXPRESSCHECKOUT_CONFIRM = "hotels.roomservice.GETEXPRESSCHECKOUT.CONFIRM" ;
        /*
         * 管家服务
         */
        public static final String HOUSEKEEPER = "hotels.roomservice.HOUSEKEEPER";
        /*
         * 管家服务确认
         */
        public static final String HOUSEKEEPER_CONFIRM = "hotels.roomservice.HOUSEKEEPER.CONFIRM";
        /*
         * 获取留言
         */
        public static final String GET_MESSAGE = "hotels.roomservice.GETMESSAGE";
        /*
         * 获取影片分类
         */
        public static final String GET_CATGROY = "hotels.roomservice.GETCATGROY";
        /*
         * 获取影片信息
         */
        public static final String GET_MOVIE = "hotels.roomservice.GETMOVIE";
        /*
         * 酒店介绍-菜单加载
         */
        public static final String GET_HOTELINTRODUCTION_MENU = "hotels.hotelintroduction.MENU" ;
        /*
         * 酒店介绍-内容展示
         */
        public static final String GET_HOTELINTRODUCTION_SHOW = "hotels.hotelintroduction.SHOW" ;
        /*
         * 电视欣赏
         */
        public static final String GET_APPRECIATE_TV = "hotels.appreciate.TV" ;
        /*
         * 电影欣赏-菜单加载
         */
        public static final String GET_APPRECIATE_MOVIE = "hotels.appreciate.MOVIE" ;
        /*
         * 电影欣赏-节目介绍
         */
        public static final String GET_APPRECIATE_MOVIE_SHOW = "hotels.appreciate.movie.SHOW" ;
        /*
         * 音乐欣赏
         */
        public static final String GET_APPRECIATE_MUSIC = "hotels.appreciate.MUSIC" ;
        /*
         * 音乐欣赏
         */
        public static final String PLAY_APPRECIATE_MUSIC = "hotels.appreciate.MUSIC.PLAY" ;
        /*
         * 滚动字幕
         */
        public static final String GET_SUBTITLE = "hotels.SUBTITLE" ;
        /*
         * 左侧菜单-左侧菜单信息获取
         */
        public static final String GET_LEFT_TOOLBAR = "hotels.left.TOOLBAR" ;
        /*
         * 导航菜单加载
         */
        public static final String GET_NAVIGATION_MENU = "hotels.navigation.MENU" ;
        /*
         * 选择语言
         */
        public static final String SELECTE_LANG = "hotels.welcome.LANG";
        
        public static final String WELCOME_INIT = "hotels.welcome.INIT";
        
        public static final String BOOT_INFO = "hotels.welcome.BOOT";
        
        /*
         * 付费返回值
         */
        public static final String GET_PAY_BACK = "hotels.PAY.BACK";
        
    }
}
