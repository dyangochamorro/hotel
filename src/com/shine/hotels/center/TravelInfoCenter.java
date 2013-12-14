package com.shine.hotels.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shine.hotels.R;
import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.service.TravelInfoService;

public class TravelInfoCenter extends BaseCenter {
    public static String[] cities;
    public static String[] codes;

    public TravelInfoCenter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {
        cities = mContext.getResources().getStringArray(R.array.code_city);
        codes = mContext.getResources().getStringArray(R.array.code);
    }

    @Override
    public void execute(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.GET_WEATHERS)) {
//            Log.d("shine", "execute Request.Action.GET_WEATHERS");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, TravelInfoService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_WEATHERS_BACK)) {
//            Log.d("shine", "execute Request.Action.GET_WEATHERS_BACK");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, TravelInfoService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_CAR)) {
//        	Log.d("shine", "execute Request.Action.GET_CAR");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, TravelInfoService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_SCHEDULE)) {
//        	Log.d("shine", "execute Request.Action.GET_SCHEDULE");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, TravelInfoService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_WORLDTIME)) {
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, TravelInfoService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_WORLDTIME_BACK)) {
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, TravelInfoService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_FLIGHTINFO)) {
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, TravelInfoService.class);
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
