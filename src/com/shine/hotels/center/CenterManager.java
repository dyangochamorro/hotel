package com.shine.hotels.center;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import android.content.Context;

public final class CenterManager {
    public static final String CENTER_NAME_SYSTEM = "com.shine.hotels.center.SystemCenter";
    public static final String CENTER_NAME_TRAVELINFO = "com.shine.hotels.center.TravelInfoCenter";
    public static final String CENTER_NAME_ROOMSERVICE = "com.shine.hotels.center.RoomServiceCenter";
    public static final String CENTER_NAME_HOTELINTRODUCTION = "com.shine.hotels.center.HotelintroductionCenter" ;
    public static final String CENTER_NAME_APPRECIATETV = "com.shine.hotels.center.AppreciatetvCenter" ;
    public static final String CENTER_NAME_APPRECIATEMOVIE = "com.shine.hotels.center.AppreciatemovieCenter" ;
    public static final String CENTER_NAME_APPRECIATEMUSIC = "com.shine.hotels.center.AppreciatemusicCenter" ;
    public static final String CENTER_NAME_SUBTITLE = "com.shine.hotels.center.SubtitleCenter" ;
    public static final String CENTER_NAME_VOD= "com.shine.hotels.center.VODCenter";
    public static final String CENTER_NAME_TOOLBAR = "com.shine.hotels.center.ToolbarCenter" ;
    public static final String CENTER_NAME_NAVIGATION_MENU = "com.shine.hotels.center.MenuCenter" ;
    
    public static final String CENTER_BROADCAST_RESULT = "center.broadcast.result";
    public static final String CENTER_BROADCAST_INT = "center.broadcast.int";
    public static final String CENTER_BROADCAST_SRC = "center.broadcast.src";
    public static final String CENTER_BROADCAST_MUSIC_DATA = "center.broadcast.musicdata";
    public static final String CENTER_BROADCAST_MUSIC_POSITION = "center.broadcast.musicposition";
    
    public static final String CENTER_BROADCAST_BACKGROUNDMUSIC_STATE = "center.broadcast.backmusic.state";

    private static CenterManager INST;
    
    private HashMap<String, BaseCenter> mCenters;
    private Context mContext;
    
    public synchronized static CenterManager get(Context context) {
        if (INST == null) {
            INST = new CenterManager(context);
        }
        
        return INST;
    }
    
    private CenterManager(Context context) {
        mCenters = new HashMap<String, BaseCenter>();
        mContext = context;
    }
    
    /**
     * 初始化时注册一些center
     */
    public void init() {
        SystemCenter system = new SystemCenter(mContext, CENTER_NAME_SYSTEM);
        system.init();
        registerCenter(system);
        
        TravelInfoCenter travel = new TravelInfoCenter(mContext, CENTER_NAME_TRAVELINFO);
        travel.init();
        registerCenter(travel);
        
        RoomServiceCenter room = new RoomServiceCenter(mContext, CENTER_NAME_ROOMSERVICE);
        room.init();
        registerCenter(room);
        
        HotelintroductionCenter hotelintroduction = new HotelintroductionCenter(mContext, CENTER_NAME_HOTELINTRODUCTION) ;
        hotelintroduction.init() ;
        registerCenter(hotelintroduction) ;
        
        AppreciatetvCenter appreciatetv = new AppreciatetvCenter(mContext, CENTER_NAME_APPRECIATETV) ;
        appreciatetv.init() ;
        registerCenter(appreciatetv) ;
        
        AppreciatemovieCenter appreciatemovie = new AppreciatemovieCenter(mContext, CENTER_NAME_APPRECIATEMOVIE) ;
        appreciatemovie.init() ;
        registerCenter(appreciatemovie) ;
        
        AppreciatemusicCenter appreciatemusic = new AppreciatemusicCenter(mContext, CENTER_NAME_APPRECIATEMUSIC) ;
        appreciatemusic.init() ;
        registerCenter(appreciatemusic) ;
        
        SubtitleCenter subtitle = new SubtitleCenter(mContext, CENTER_NAME_SUBTITLE) ;
        subtitle.init() ;
        registerCenter(subtitle) ;
        
        VODCenter vod = new VODCenter(mContext, CENTER_NAME_VOD);
        vod.init();
        registerCenter(vod);
        
        ToolbarCenter toolbar = new ToolbarCenter(mContext, CENTER_NAME_TOOLBAR) ;
        toolbar.init() ;
        registerCenter(toolbar) ;
        
        MenuCenter menu = new MenuCenter(mContext, CENTER_NAME_NAVIGATION_MENU) ;
        menu.init() ;
        registerCenter(menu) ;
    }
    
    /**
     * 通过名字得到center
     * @param name
     * @return
     */
    public BaseCenter getCenterByName(String name) {
        BaseCenter center = mCenters.get(name);
        if (center == null) {
            try {
                @SuppressWarnings("unchecked")
                Class<BaseCenter> clazz = (Class<BaseCenter>)Class.forName(name);
                Constructor<BaseCenter> constructor = clazz.getConstructor(Context.class, String.class);
                center = constructor.newInstance(mContext, name);
                center.init();
                
                registerCenter(center);
                
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
        return center;
    }
    
    public void registerCenter(BaseCenter center) {
        mCenters.put(center.getName(), center);
    }
    
    public void unregisterCenter(String name) {
        mCenters.remove(name);
    }
}
