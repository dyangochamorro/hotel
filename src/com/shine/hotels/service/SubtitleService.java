
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
import com.shine.hotels.io.model.Subtitle;
import com.shine.hotels.io.model.TestData;
import com.shine.hotels.util.Utils;

public class SubtitleService extends IntentService {
    private NetEngine mEngine;

    public SubtitleService() {
        super("Subtitle");
        mEngine = new NetEngine();
    }

    @Override
    protected void onHandleIntent(Intent requestIntent) {
        try {
            String action = requestIntent.getAction();
            if (action.equals(Request.Action.GET_SUBTITLE)) {
            	Subtitle subtitle = mEngine.httpGetRequest(APIManager.API_GET_SUBTITLE,
                        requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new SubtitleHandler());
                
//            	if (null == subtitle) {
//            		SubtitleHandler handler = new SubtitleHandler();
//                    String response = TestData.Subtitle;
//                    subtitle = handler.handleResponse(response);
//            	}
                
                Intent intentRes = new Intent(BroadcastAction.SUBTITLE);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, subtitle);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class SubtitleHandler implements IResponseHandler<Subtitle> {
        
    	@Override
        public Subtitle handleResponse(String response) {
    		Subtitle subtitle = null ;
            
    		JSONObject json = null ;
            try {
                    json = new JSONObject(response) ;
                    
                    subtitle = new Subtitle() ;
                    
                    subtitle.setScrolltextcontent(json.getString("scrolltextcontent"));
                    subtitle.setScrolltextstarttime(json.getString("scrolltextstarttime"));
                    subtitle.setScrolltextendtime(json.getString("scrolltextendtime"));
                    subtitle.setScrolltextbackgourd(json.getString("scrolltextbackgourd"));
                    subtitle.setScrolltextcolor(json.getString("scrolltextcolor"));
                    subtitle.setScrolltextsize(json.getString("scrolltextsize"));
                    subtitle.setScrolltextcoordinate(json.getString("scrolltextcoordinate"));
                    subtitle.setScrolltexttempo(json.getString("scrolltexttempo"));
                    
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            return subtitle ;

        }

    }
}
