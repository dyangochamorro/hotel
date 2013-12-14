package com.shine.hotels.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.service.RoomService;

public class RoomServiceCenter extends BaseCenter {

    public RoomServiceCenter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.GET_BILLING)) {
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, RoomService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_PHONE_NO)) {
            Bundle param = request.getParam();
            Intent intent = new Intent(mContext, RoomService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_MESSAGE)) {
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, RoomService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.HOUSEKEEPER)) {
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, RoomService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.HOUSEKEEPER_CONFIRM)) {
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, RoomService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_EXPRESSCHECKOUT)) {
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, RoomService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        } else if (action.equals(Request.Action.GET_EXPRESSCHECKOUT_CONFIRM)) {
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, RoomService.class);
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
