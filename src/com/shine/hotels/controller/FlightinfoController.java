package com.shine.hotels.controller;

import java.util.List;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.io.model.Flight;
import com.shine.hotels.io.model.FlightList;
import com.shine.hotels.io.model.UsefulTelList;

import de.greenrobot.event.EventBus;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class FlightinfoController extends BaseController {
    FlightList[] lists;

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
        filter.addAction(BroadcastAction.TRAVELINFO_GETFLIGHTINFO);
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
        if (action.equals(Request.Action.GET_FLIGHTINFO)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_TRAVELINFO);

            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.TRAVELINFO_GETFLIGHTINFO)) {
            lists = (FlightList[])intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetResultEvent<FlightList[]> event = new GetResultEvent<FlightList[]>();
            if (lists != null) {
                event.result = lists;
            }
            EventBus.getDefault().post(event);
        }
    }
    
    public List<Flight> getFlights(int position) {
        if (lists == null || position < 0 || position > lists.length) return null;
        
        return lists[position].getFlights();
    }

}
