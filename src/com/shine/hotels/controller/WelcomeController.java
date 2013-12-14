package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.BootInfoEvent;
import com.shine.hotels.controller.Events.WelcomeEvent;
import com.shine.hotels.io.model.BootInfo;
import com.shine.hotels.io.model.WelcomeList;

import de.greenrobot.event.EventBus;

public class WelcomeController extends BaseController {

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
        filter.addAction(BroadcastAction.HOTEL_HOMEPAGE);
        filter.addAction(BroadcastAction.BOOT_INFO);
        
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
        if (action.equals(Request.Action.SELECTE_LANG) ||
                action.equals(Request.Action.BOOT_INFO) ||
                action.equals(Request.Action.WELCOME_INIT)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_SYSTEM);

            center.execute(request);
        } 
    }

    @Override
    public void onReceive(Context context, Intent intent) {
    	String action = intent.getAction();
        if (action.equals(BroadcastAction.HOTEL_HOMEPAGE)) {
            WelcomeList list = (WelcomeList)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            WelcomeEvent<WelcomeList> event = new WelcomeEvent<WelcomeList>();
            if (list != null) {
                event.result = list;
            }
            EventBus.getDefault().post(event);
            
        } else if (action.equals(BroadcastAction.BOOT_INFO)) {
            BootInfo info = (BootInfo)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            BootInfoEvent event = new BootInfoEvent();
            if (info != null) {
                event.bootInfo = info;
            }
            EventBus.getDefault().post(event);
        }
    }

}
