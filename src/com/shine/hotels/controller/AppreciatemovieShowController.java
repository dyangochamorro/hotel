package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.AppreciatemovieShowEvent;
import com.shine.hotels.io.model.AppreciatemovieShow;

import de.greenrobot.event.EventBus;

public class AppreciatemovieShowController extends BaseController {

    @Override
    protected void onCreate() {
    }

    @Override
    protected void onDestroy() {
    }

    @Override
    protected IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.APPRECIATE_MOVIE_SHOW);
        
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
        if (action.equals(Request.Action.GET_APPRECIATE_MOVIE_SHOW)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_APPRECIATEMOVIE);

            center.execute(request);
        } else if (action.equals(Request.Action.GET_PAY_BACK)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_APPRECIATEMOVIE);

            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.APPRECIATE_MOVIE_SHOW)) {
            AppreciatemovieShow showData = (AppreciatemovieShow)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            AppreciatemovieShowEvent event = new AppreciatemovieShowEvent();
            if (showData != null) {
                event.result = showData;
                EventBus.getDefault().post(event);
            }
        }
    }

}
