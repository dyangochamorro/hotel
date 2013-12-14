
package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetAllWeather;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.io.model.WeatherList;
import com.shine.hotels.ui.guidebook.FragmentWeather;

import de.greenrobot.event.EventBus;

public class TravelController extends BaseController {
    
    
    @Override
    protected void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.TRAVELINFO_GETWEATHER);
        return filter;
    }

    @Override
    protected boolean onUpdateRequest(Request oldOne, Request newOne) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onHandle(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.GET_WEATHERS) || action.equals(Request.Action.GET_WEATHERS_BACK)) {
//            Log.d("shine", "controller Request.Action.GET_...");
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_TRAVELINFO);
            
            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.TRAVELINFO_GETWEATHER)) {
//            Log.d("shine", "onReceive BROADCAST_ACTOIN_GETWEATHER");
            WeatherList list = (WeatherList)intent
                    .getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);

            String src = intent.getStringExtra(CenterManager.CENTER_BROADCAST_SRC);
            if (!TextUtils.isEmpty(src)) {
            	if (FragmentWeather.CITY_STATE.equals(src)) {
            		GetResultEvent<WeatherList> event = new GetResultEvent<WeatherList>();
                    if (list != null) {
                        event.result = list;
                    }
                    EventBus.getDefault().post(event);
            	} else {
            		GetAllWeather event = new GetAllWeather();
                    event.data = list;
                    
                    EventBus.getDefault().post(event);
            	}
                
            }
        } 
//        else if (action.equals(BroadcastAction.TRAVELINFO_GETALLWEATHER)) {
//            
//            Log.d("shine", "onReceive BROADCAST_ACTOIN_GETALLWEATHER");
//            WeatherList list = (WeatherList)intent
//                    .getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
//            GetAllWeather event = new GetAllWeather();
//            event.data = list;
//            
//            EventBus.getDefault().post(event);
//        }
            
    }
    
    public void onEvent(Object event) {
        
    }

}
