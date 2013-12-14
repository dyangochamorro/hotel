package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.io.model.UsefulTelList;

import de.greenrobot.event.EventBus;

public class RoomServiceController extends BaseController {
    public static final int ID_MESSAGE = 1;

    @Override
    protected void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.ROOMSERVICE_GETUSEFULTEL);
        
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
        if (action.equals(Request.Action.GET_PHONE_NO)) {
//            Log.d("shine", "controller Request.Action.GET_...");
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_ROOMSERVICE);
            
            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.ROOMSERVICE_GETUSEFULTEL)) {
//            Log.d("shine", "onReceive ROOMSERVICE_GETUSEFULTEL");
            UsefulTelList list = (UsefulTelList)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetResultEvent<UsefulTelList> event = new GetResultEvent<UsefulTelList>();
            event.result = list;
            EventBus.getDefault().post(event);
        }
    }

}
