package com.shine.hotels.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.service.AppreciatemusicService;

public class AppreciatemusicCenter extends BaseCenter {

    public AppreciatemusicCenter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {

    }

    @Override
    public void execute(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.GET_APPRECIATE_MUSIC)) {
//            Log.d("shine", "execute Request.Action.GET_APPRECIATE_MUSIC");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, AppreciatemusicService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } 
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
    
}
