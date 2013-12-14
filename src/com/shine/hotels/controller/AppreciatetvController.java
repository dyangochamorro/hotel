package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetTvEvent;
import com.shine.hotels.io.model.AppreciatetvList;

import de.greenrobot.event.EventBus;

public class AppreciatetvController extends BaseController {

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
        filter.addAction(BroadcastAction.APPRECIATE_TV);
        
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
        if (action.equals(Request.Action.GET_APPRECIATE_TV)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_APPRECIATETV);

            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.APPRECIATE_TV)) {
            AppreciatetvList list = (AppreciatetvList)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetTvEvent<AppreciatetvList> event = new GetTvEvent<AppreciatetvList>();
            if (list != null) {
                event.result = list;
            }
            EventBus.getDefault().post(event);
        }
    }

}
