package com.shine.hotels.center;

import java.io.IOException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.io.IResponseHandler;
import com.shine.hotels.io.NetEngine;
import com.shine.hotels.io.model.ScrollText;
import com.shine.hotels.service.MenuService;
import com.shine.hotels.service.RoomService;
import com.shine.hotels.service.SystemLocalService;
import com.shine.hotels.service.SystemLocalService.SystemLocalBinder;

public class SystemCenter extends BaseCenter {
    private SystemLocalService mLocalService;
    boolean mBound = false;
    
    private ServiceConnection mConnection = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SystemLocalBinder binder = (SystemLocalBinder)service;
            mLocalService = binder.getService();
            mBound = true;
        }
    };

    public SystemCenter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {
        Intent intent = new Intent(mContext, SystemLocalService.class);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        
        // 获取系统配置
        
    }

    @Override
    public void execute(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.WATCH)) {
            
        } else if (action.equals(Request.Action.MARQUEE)) {
        } else if (action.equals(Request.Action.PLAY_BACKGROUND_MUSIC)) {
            
        } else if (action.equals(Request.Action.SELECTE_LANG)) {
//            Log.d("shine", "execute Request.Action.SELECTE_LANG");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, SystemLocalService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.WELCOME_INIT)) {
//        	Log.d("shine", "execute Request.Action.WELCOME_INIT");
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, SystemLocalService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.BOOT_INFO)) {
//            Log.d("shine", "execute Request.Action.BOOT_INFO");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, SystemLocalService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        }
        
    }

    @Override
    public void destroy() {
        if (mBound) {
            mContext.unbindService(mConnection);
            mBound = false;
        }
    }
    
    private class GetMarqueeTask extends AsyncTask<Void, Void, ScrollText> {

        @Override
        protected ScrollText doInBackground(Void... params) {
            // TODO Auto-generated method stub
            NetEngine engine = new NetEngine();
            try {
                ScrollText text = engine.httpGetRequest(APIManager.API_GET_APPRECIATE_MOVIE,
                        null, new MarqueeHandler());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ScrollText result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
        
    }
    
    private class MarqueeHandler implements IResponseHandler<ScrollText> {

        @Override
        public ScrollText handleResponse(String response) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
    
}
