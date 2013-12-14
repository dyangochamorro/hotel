
package com.shine.hotels.service;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;

import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.io.IResponseHandler;
import com.shine.hotels.io.NetEngine;
import com.shine.hotels.io.model.TestData;
import com.shine.hotels.io.model.ToolBarData;
import com.shine.hotels.util.Utils;

public class ToolbarService extends IntentService {
    private NetEngine mEngine;

    public ToolbarService() {
        super("Toolbar");
        mEngine = new NetEngine();
    }

    @Override
    protected void onHandleIntent(Intent requestIntent) {
        try {
            String action = requestIntent.getAction();
            if (action.equals(Request.Action.GET_LEFT_TOOLBAR)) {
            	ToolBarData toolbar = mEngine.httpGetRequest(APIManager.API_GET_TOOLBAR,
                        requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new ToolbarHandler());
                
            	if (null == toolbar) {
            		ToolbarHandler handler = new ToolbarHandler();
                    String response = TestData.Toolbar;
                    toolbar = handler.handleResponse(response);
            	}
                
                Intent intentRes = new Intent(BroadcastAction.MESSAGE_RECV_MESSAGE);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, toolbar);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class ToolbarHandler implements IResponseHandler<ToolBarData> {
        
    	@Override
        public ToolBarData handleResponse(String response) {
    		ToolBarData toolbar = null ;
            
    		JSONObject json ;
            try {
            	json = new JSONObject(response) ;

            	toolbar = new ToolBarData();
            	
            	toolbar.setCity(json.getString("city")) ;
            	toolbar.setWeather(json.getString("weather")) ;
            	toolbar.setTemperature(json.getString("temperature")) ;
            	toolbar.setMsgNum(json.getInt("msgnum")) ;
            	toolbar.setLogo(json.getString("logo"));
                    
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return toolbar ;

        }

    }
}
