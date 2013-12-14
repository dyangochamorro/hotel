package com.shine.hotels.controller;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * controller
 */
public abstract class BaseController extends BroadcastReceiver {
    protected Context mContext;
    
//    private SparseArray<Request> mRequests = new SparseArray<Request>();
    private HashMap<String, Request> mRequests = new HashMap<String, Request>();
    
    
    public void handle(Request request) {
        final String key = request.getAction();
        Request r = mRequests.get(key);
        if (r != null) {
            if (onUpdateRequest(r, request)) {
                mRequests.remove(key);
                mRequests.put(key, request);
                
                cancelRequest(r);
            } else {
                
                cancelRequest(request);
                return;
            }
        }
        
        request.state = Request.STATE_RUNNING;
        // 由实现类处理具体逻辑
        onHandle(request);
    }
    
    public void response(int key) {
        Request r = mRequests.get(key);
        r.state = Request.STATE_FINISHED;
        mRequests.remove(key);
    }
    
    protected void cancelRequest(Request request) {
        request.state = Request.STATE_CANCELED;
        request = null;
    }
    
    public void create(Context context) {
        mContext = context;
        onCreate();
    }
    
    public void destroy() {
        onDestroy();
        
        mRequests.clear();
        mRequests = null;
    }
    
    public void onEvent(Object event) {}

    protected abstract void onCreate();

    protected abstract void onDestroy();
    
    protected abstract IntentFilter getFilter();
    
    /**
     * 比较相同类型的请求出现重复时，是保留新的还是旧的
     * @param oldOne
     * @param newOne
     * @return true：保留新的；false：保留旧的
     */
    protected abstract boolean onUpdateRequest(Request oldOne, Request newOne);
    /**
     * 子类处理逻辑
     * @param request
     */
    protected abstract void onHandle(Request request);
    
}
