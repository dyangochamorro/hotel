package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetToolDataEvent;
import com.shine.hotels.controller.Events.GetTvEvent;
import com.shine.hotels.controller.Events.SubtitleEvent;
import com.shine.hotels.io.model.AppreciatetvList;
import com.shine.hotels.io.model.Subtitle;
import com.shine.hotels.io.model.ToolBarData;

import de.greenrobot.event.EventBus;

public class SubtitleController extends BaseController {

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
        filter.addAction(BroadcastAction.SUBTITLE);
        filter.addAction(BroadcastAction.APPRECIATE_TV);
        filter.addAction(BroadcastAction.MESSAGE_RECV_MESSAGE);
        
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
        if (action.equals(Request.Action.GET_SUBTITLE)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_SUBTITLE);

            center.execute(request);
        } else if (action.equals(Request.Action.GET_APPRECIATE_TV)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_APPRECIATETV);

            center.execute(request);
        } else if (action.equals(Request.Action.GET_LEFT_TOOLBAR)) {
//            Log.d("shine", "controller Request.Action.GET_LEFT_TOOLBAR");
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_TOOLBAR);
            
            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.SUBTITLE)) {
            Subtitle subtitle = (Subtitle)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            SubtitleEvent event = new SubtitleEvent();
            if (subtitle != null) {
                event.result = subtitle;
            }
            EventBus.getDefault().post(event);
        } else if (action.equals(BroadcastAction.APPRECIATE_TV)) {
            AppreciatetvList list = (AppreciatetvList)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetTvEvent<AppreciatetvList> event = new GetTvEvent<AppreciatetvList>();
            if (list != null) {
                event.result = list;
            }
            EventBus.getDefault().post(event);
        } else if (action.equals(BroadcastAction.MESSAGE_RECV_MESSAGE)) {
//            Log.d("shine", "onReceive MESSAGE_RECV_MESSAGE");
            ToolBarData toolbar = (ToolBarData)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetToolDataEvent event = new GetToolDataEvent();
            event.data = toolbar ;
            EventBus.getDefault().post(event);
            
        } 
    }

}
