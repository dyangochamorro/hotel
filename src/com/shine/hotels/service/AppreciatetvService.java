
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
import com.shine.hotels.io.model.Appreciatetv;
import com.shine.hotels.io.model.AppreciatetvList;
import com.shine.hotels.io.model.CategoryData;
import com.shine.hotels.io.model.TestData;
import com.shine.hotels.util.Utils;

public class AppreciatetvService extends IntentService {
    private NetEngine mEngine;

    public AppreciatetvService() {
        super("Appreciatetv");
        mEngine = new NetEngine();
    }

    @Override
    protected void onHandleIntent(Intent requestIntent) {
        try {
            String action = requestIntent.getAction();
            if (action.equals(Request.Action.GET_APPRECIATE_TV)) {
            	AppreciatetvList list = mEngine.httpGetRequest(APIManager.API_GET_APPRECIATE_TV,
                        requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new AppreciatetvHandler());
                
            	if (null == list) {
            		AppreciatetvHandler handler = new AppreciatetvHandler();
                    String response = TestData.Appreciatetv;
                    list = handler.handleResponse(response);
            	}
                
                Intent intentRes = new Intent(BroadcastAction.APPRECIATE_TV);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class AppreciatetvHandler implements IResponseHandler<AppreciatetvList> {
        
    	@Override
        public AppreciatetvList handleResponse(String response) {
    		AppreciatetvList lists = null ;
        	List<Appreciatetv> mList = null ;
            
            JSONArray array;
            try {
                array = new JSONArray(response);
                int size = array.length();
                lists = new AppreciatetvList() ;
                mList = new ArrayList<Appreciatetv>() ;
                
                for(int i = 0; i < size; i++) {
                    JSONObject json = array.getJSONObject(i);
                    Appreciatetv appreciatetv = new Appreciatetv() ;
                    
                    appreciatetv.setCategorytypename(json.getString("categorytypename"));
                    
                    ArrayList<CategoryData> datas = new ArrayList<CategoryData>();
                    JSONArray subArray = json.optJSONArray("categorys");
                    for (int j = 0; j < subArray.length(); j++) {
                        JSONObject jdata = subArray.optJSONObject(j);
                        if (jdata != null) {
                        	CategoryData data = new CategoryData();
                        	data.setCategoryname(jdata.optString("categoryname"));
                        	data.setCategorypic(jdata.optString("categorypic")) ;
                        	data.setCategoryurl(jdata.optString("categoryurl")) ;
                            
                        	datas.add(data);
                        }
                    }
                    
                    appreciatetv.setData(datas) ;
                    
                    mList.add(appreciatetv) ;
                    lists.setList(mList) ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            
            return lists ;

        }

    }
}
