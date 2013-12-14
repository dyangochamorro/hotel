package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetWorldTimes;
import com.shine.hotels.controller.Events.GetWorldTimesRight;
import com.shine.hotels.io.model.WorldTimeList;
import com.shine.hotels.ui.guidebook.FragmentWorldtime;

import de.greenrobot.event.EventBus;

public class WorldTimeController extends BaseController {

    @Override
    protected void onCreate() {
        //EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        //EventBus.getDefault().unregister(this);
    }

    @Override
    protected IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.TRAVELINFO_GETWORLDTIME);
        
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
        if (action.equals(Request.Action.GET_WORLDTIME) || action.equals(Request.Action.GET_WORLDTIME_BACK)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_TRAVELINFO);

            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.TRAVELINFO_GETWORLDTIME)) {
            WorldTimeList list = (WorldTimeList)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            String src = intent.getStringExtra(CenterManager.CENTER_BROADCAST_SRC);
            if (!TextUtils.isEmpty(src) && list!=null) {
            	if (FragmentWorldtime.CITY_STATE.equals(src)) {
            		GetWorldTimes event = new GetWorldTimes();
                    event.data = list;
                    EventBus.getDefault().post(event);
            	} else {
            		GetWorldTimesRight event = new GetWorldTimesRight();
                    event.data = list;
                    
                    EventBus.getDefault().post(event);
            	}
                
            }
        }
    }

}
