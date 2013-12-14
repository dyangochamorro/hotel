package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.io.model.AppreciatemusicList;

import de.greenrobot.event.EventBus;

public class AppreciatemusicController extends BaseController {

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
        filter.addAction(BroadcastAction.APPRECIATE_MUSIC);
        
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
        if (action.equals(Request.Action.GET_APPRECIATE_MUSIC)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_APPRECIATEMUSIC);

            center.execute(request);
            
        } else if (action.equals(Request.Action.PLAY_APPRECIATE_MUSIC)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_VOD);

            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.APPRECIATE_MUSIC)) {
            AppreciatemusicList list = (AppreciatemusicList)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetResultEvent<AppreciatemusicList> event = new GetResultEvent<AppreciatemusicList>();
            if (list != null) {
                event.result = list;
            }
            EventBus.getDefault().post(event);
        }
    }

}
