package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.GetResultEvent;
import com.shine.hotels.io.model.MenuList;

import de.greenrobot.event.EventBus;

/**
 * 导航菜单加载
 * @author guoliang
 *
 */
public class MenuController extends BaseController {

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
        filter.addAction(BroadcastAction.HOTEL_NAVIGATION_MENU);
        
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
        if (action.equals(Request.Action.GET_NAVIGATION_MENU)) {
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_NAVIGATION_MENU);

            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.HOTEL_NAVIGATION_MENU)) {
            MenuList list = (MenuList)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetResultEvent<MenuList> event = new GetResultEvent<MenuList>();
            if (list != null) {
                event.result = list;
            }
            EventBus.getDefault().post(event);
        }
    }

}
