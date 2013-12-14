package com.shine.hotels.center;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.shine.hotels.controller.Request;

/**
 * 负责各业务逻辑的基础类
 */
public abstract class BaseCenter {
    protected Context mContext;
    protected String mName;
    
    public BaseCenter(Context context, String name) {
        mContext = context;
        mName = name;
    }
    
    public String getName() {
        return mName;
    }
    
    protected void notifyByLocalBroadcast(Intent localIntent) {
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(localIntent);
    }
    
    protected void notifyByGlobalBroadcast(Intent intent) {
        mContext.sendBroadcast(intent);
    }

    public abstract void init();
    
    public abstract void destroy();
    
    public abstract void execute(Request request);
}
