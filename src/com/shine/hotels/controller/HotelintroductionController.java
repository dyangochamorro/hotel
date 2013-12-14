package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.io.model.HotelintroductionMenuList;

import de.greenrobot.event.EventBus;

public class HotelintroductionController extends BaseController {

    @Override
    protected void onCreate() {
    }

    @Override
    protected void onDestroy() {
    }

    @Override
    protected IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.HOTEL_INTRODUCTION_MENU);
        
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
        if (action.equals(Request.Action.GET_HOTELINTRODUCTION_MENU)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_HOTELINTRODUCTION);

            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.HOTEL_INTRODUCTION_MENU)) {
            HotelintroductionMenuList list = (HotelintroductionMenuList)intent
                    .getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetResultEvent<HotelintroductionMenuList> event = new GetResultEvent<HotelintroductionMenuList>();
            if (list != null) {
                event.result = list;
            }
            EventBus.getDefault().post(event);
        }
    }

}
