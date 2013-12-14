package com.shine.hotels.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.service.AppreciatemovieService;

public class AppreciatemovieCenter extends BaseCenter {

    public AppreciatemovieCenter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.GET_APPRECIATE_MOVIE)) {
//            Log.d("shine", "execute Request.Action.GET_APPRECIATE_MOVIE");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, AppreciatemovieService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_APPRECIATE_MOVIE_SHOW)) {
//            Log.d("shine", "execute Request.Action.GET_APPRECIATE_MOVIE_SHOW");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, AppreciatemovieService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_PAY_BACK)) {
//            Log.d("shine", "execute Request.Action.GET_PAY_BACK");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, AppreciatemovieService.class);
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
