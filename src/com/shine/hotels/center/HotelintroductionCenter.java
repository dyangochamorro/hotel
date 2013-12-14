package com.shine.hotels.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.service.HotelintroductionService;

public class HotelintroductionCenter extends BaseCenter {

    public HotelintroductionCenter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.GET_HOTELINTRODUCTION_MENU)) {
//            Log.d("shine", "execute Request.Action.GET_HOTELINTRODUCTION_MENU");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, HotelintroductionService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_HOTELINTRODUCTION_SHOW)) {
//            Log.d("shine", "execute Request.Action.GET_HOTELINTRODUCTION_SHOW");
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, HotelintroductionService.class);
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
