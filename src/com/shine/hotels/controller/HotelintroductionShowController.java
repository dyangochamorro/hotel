package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.HotelintroductionShowEvent;
import com.shine.hotels.io.model.HotelintroductionShow;

import de.greenrobot.event.EventBus;

public class HotelintroductionShowController extends BaseController {

    @Override
    protected void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.HOTEL_INTRODUCTION_SHOW);
        
        return filter;
    }

    @Override
    protected boolean onUpdateRequest(Request oldOne, Request newOne) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onHandle(Request request) {
//        Log.w("shine", "onHandle GET_HOTELINTRODUCTION_SHOW");
        String action = request.getAction();
        if (action.equals(Request.Action.GET_HOTELINTRODUCTION_SHOW)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_HOTELINTRODUCTION);

            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.w("shine", "onReceive GET_HOTELINTRODUCTION_SHOW");
        String action = intent.getAction();
        if (action.equals(BroadcastAction.HOTEL_INTRODUCTION_SHOW)) {
            HotelintroductionShow showData = (HotelintroductionShow)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            HotelintroductionShowEvent event = new HotelintroductionShowEvent();
            if (showData != null) {
                event.result = showData;
            }
            EventBus.getDefault().post(event);
        }
    }

}
