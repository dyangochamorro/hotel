
package com.shine.hotels.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import com.shine.hotels.io.model.Appreciatemusic;
import com.shine.hotels.io.model.AppreciatemusicList;
import com.shine.hotels.io.model.MusicData;
import com.shine.hotels.io.model.TestData;
import com.shine.hotels.util.Utils;

public class AppreciatemusicService extends IntentService {
    private NetEngine mEngine;

    public AppreciatemusicService() {
        super("Appreciatemusic");
        mEngine = new NetEngine();
    }

    @Override
    protected void onHandleIntent(Intent requestIntent) {
        try {
            String action = requestIntent.getAction();
            if (action.equals(Request.Action.GET_APPRECIATE_MUSIC)) {
            	AppreciatemusicList list = mEngine.httpGetRequest(APIManager.API_GET_APPRECIATE_MUSIC,
                        requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new AppreciatemusicHandler());
                
            	if (null == list) {
                	AppreciatemusicHandler handler = new AppreciatemusicHandler();
                    String response = TestData.Appreciatemusic;
                    list = handler.handleResponse(response);
            	}
                
                Intent intentRes = new Intent(BroadcastAction.APPRECIATE_MUSIC);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class AppreciatemusicHandler implements IResponseHandler<AppreciatemusicList> {
        
    	@Override
        public AppreciatemusicList handleResponse(String response) {
    		AppreciatemusicList lists = null ;
        	List<Appreciatemusic> mList = null ;
            
            JSONArray array;
            try {
                array = new JSONArray(response);
                int size = array.length();
                lists = new AppreciatemusicList() ;
                mList = new ArrayList<Appreciatemusic>() ;
                
                for(int i = 0; i < size; i++) {
                    JSONObject json = array.getJSONObject(i);
                    Appreciatemusic appreciatemusic = new Appreciatemusic() ;
                    
                    appreciatemusic.setMusictypepic(json.getString("musictypepic"));
                    appreciatemusic.setSpecialposter(json.getString("specialposter"));
                    appreciatemusic.setMusicspecialname(json.getString("musicspecialname")) ;
                    
                    ArrayList<MusicData> datas = new ArrayList<MusicData>();
                    JSONArray subArray = json.optJSONArray("musics");
                    for (int j = 0; j < subArray.length(); j++) {
                        JSONObject jdata = subArray.optJSONObject(j);
                        if (jdata != null) {
                        	MusicData data = new MusicData();
                        	data.setMusicname(jdata.optString("musicname"));
                        	data.setMusicurl(jdata.optString("musicurl")) ;
                            
                        	datas.add(data);
                        }
                    }
                    
                    appreciatemusic.setData(datas) ;
                    
                    mList.add(appreciatemusic) ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            lists.setList(mList) ;
            
            return lists ;

        }

    }
}
