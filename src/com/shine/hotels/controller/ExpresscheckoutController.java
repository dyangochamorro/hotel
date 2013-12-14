package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.io.model.ExpresscheckoutList;

import de.greenrobot.event.EventBus;

public class ExpresscheckoutController extends BaseController {

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
        filter.addAction(BroadcastAction.ROOMSERVICE_EXPRESSCHECKOUT);
        
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
        if (action.equals(Request.Action.GET_EXPRESSCHECKOUT)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_ROOMSERVICE);

            center.execute(request);
        } else if (action.equals(Request.Action.GET_EXPRESSCHECKOUT_CONFIRM)) {
        	BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_ROOMSERVICE);

            center.execute(request);
        }
        	
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.ROOMSERVICE_EXPRESSCHECKOUT)) {
            ExpresscheckoutList list = (ExpresscheckoutList)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetResultEvent<ExpresscheckoutList> event = new GetResultEvent<ExpresscheckoutList>();
            if (list != null) {
                event.result = list;
            }
            EventBus.getDefault().post(event);
        }
    }

}
