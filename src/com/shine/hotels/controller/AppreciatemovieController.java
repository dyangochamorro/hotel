package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.io.model.AppreciatemovieList;
import com.shine.hotels.io.model.AppreciatetvList;

import de.greenrobot.event.EventBus;

public class AppreciatemovieController extends BaseController {

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
        filter.addAction(BroadcastAction.APPRECIATE_MOVIE);
        
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
        if (action.equals(Request.Action.GET_APPRECIATE_MOVIE)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_APPRECIATEMOVIE);

            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.APPRECIATE_MOVIE)) {
            AppreciatemovieList list = (AppreciatemovieList)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetResultEvent<AppreciatemovieList> event = new GetResultEvent<AppreciatemovieList>();
            if (list != null) {
                event.result = list;
                EventBus.getDefault().post(event);
            }
        }
    }

}
