package com.shine.hotels.io;
/**
 * 统一管理网络接口 
 *
 */
public final class APIManager {
    public static final String HTTP_PARAM_KEY = "http_param";
    // 公网：124.205.155.95
    // 内网：172.168.11.11
    public static final String DEFAULT_HOST = "http://172.168.11.11/hotel/";
    public static String HOST = "";

    // 旅行指南-天气预报
    public static final String API_GET_WEATHER = "weather.php";
    // 旅行指南-天气预报返回值
    public static final String API_GET_WEATHER_BACK = "weather_back.php";
    // 旅行指南-世界时间
    public static final String API_GET_WORLDTIME = "worldtime.php";
    // 旅行指南-世界时间返回值
    public static final String API_GET_WORLDTIME_BACK = "worldtime_back.php";
    
    public static final String API_GET_ROOMSERVICE = "";
    // 客房服务-常用电话
    public static final String API_GET_USEFUL_TEL = "usefultel.php";
    // 客房服务-账单查询
    public static final String API_GET_BILLQUERY = "billquery.php";
    // 客房服务-快速结帐
    public static final String API_GET_EXPRESSCHECKOUT = "expresscheckout.php";
    // 客房服务-快速结帐确认
    public static final String API_GET_EXPRESSCHECKOUT_CONFIRM = "expresscheckout_confirm.php";
    // 客房服务-管家服务
    public static final String API_GET_BUTLERSERVICE = "butlerservice.php";
    // 客房服务-管家服务确认
    public static final String API_GET_BUTLERSERVICE_CONFIRM = "butlerservice_confirm.php";
    // 客房服务-留言服务
    public static final String API_GET_MEMOSERV = "memoserv.php";
    // 旅行指南-租车服务
    public static final String API_GET_CARRENTAL = "carrental.php";
    // 旅行指南-列车信息
    public static final String API_GET_TRAININFOMATION = "traininfomation.php";
    // 旅行指南-航班信息
    public static final String API_GET_FLIGHTINFOMATION = "flightinfo.php" ;
    // 酒店介绍-菜单加载
    public static final String API_GET_HOTELINTRODUCTION_MENU = "hotelintroduction_index.php";
    // 酒店介绍-内容展示
    public static final String API_GET_HOTELINTRODUCTION_SHOW = "hotelintroduction_show.php";
    // 电视欣赏
    public static final String API_GET_APPRECIATE_TV = "appreciatetv.php";
    // 电影欣赏-菜单加载
    public static final String API_GET_APPRECIATE_MOVIE = "appreciatemovie_index.php";
    // 电影欣赏-节目介绍
    public static final String API_GET_APPRECIATE_MOVIE_SHOW = "appreciatemovie_introduce.php";
    // 电影欣赏-节目介绍
    public static final String API_GET_APPRECIATE_MOVIE_PAY = "appreciatemovie_pay.php";
    // 音乐欣赏
    public static final String API_GET_APPRECIATE_MUSIC = "appreciatemusic.php";
    // 滚动字幕
    public static final String API_GET_SUBTITLE = "scrolltext.php";
    // 左侧菜单
    public static final String API_GET_TOOLBAR = "toolbar.php" ;
    // 导航菜单加载
    public static final String API_GET_NAVIGATION_MENU = "menu.php" ;
    // 语言选择
    public static final String API_SEL_LANG = "select_lang.php";
    
    public static final String API_WELCOME_INIT = "welcome.php";
    
    public static final String API_BOOT_INFO = "startslide.php";
}
