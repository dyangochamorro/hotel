
package com.shine.hotels.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.io.IResponseHandler;
import com.shine.hotels.io.NetEngine;
import com.shine.hotels.io.model.Menu;
import com.shine.hotels.io.model.MenuList;
import com.shine.hotels.io.model.TestData;
import com.shine.hotels.util.Utils;

public class MenuService extends IntentService {
    private NetEngine mEngine;

    public MenuService() {
        super("NavigationMenu");
        mEngine = new NetEngine();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
	        String action = intent.getAction();
	
	        if (action.equals(Request.Action.GET_NAVIGATION_MENU)) {
//	            Log.d("shine", "onHandleIntent Request.Action.GET_NAVIGATION_MENU");
	            // 网络请求
	            MenuList list = mEngine.httpGetRequest(APIManager.API_GET_NAVIGATION_MENU, 
	            		intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new MenuHandler());
	            
	            // 本地测试
	            if (null == list) {
	            	Bundle bundle = intent.getBundleExtra(APIManager.HTTP_PARAM_KEY) ;
		            String tag = (String) bundle.get("tag") ;
		            
		            MenuHandler handler = new MenuHandler();
		            
		            String response = "" ;
		            if ("SY".equals(tag)) {
		            	response = TestData.SY_MENU ;
		            } else if ("KFFW".equals(tag)) {
		            	response = TestData.KFFW_MENU ;
		            } else if ("LXZN".equals(tag)) {
		            	response = TestData.LXZN_MENU ;
		            } else if ("DBFW".equals(tag)) {
		            	response = TestData.DBFW_MENU ;
		            }
		
		            list = handler.handleResponse(response);
	            }
	
	            Intent intentRes = new Intent(BroadcastAction.HOTEL_NAVIGATION_MENU);
	            intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
	            Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
	        } 
        } catch (Exception e) {
        	e.printStackTrace() ;
        }
    }

    private class MenuHandler implements IResponseHandler<MenuList> {

        @Override
        public MenuList handleResponse(String response) {
            MenuList lists = null ;
            List<Menu> mList = null ;

            JSONArray array;
            try {
                array = new JSONArray(response);
                int size = array.length();
                lists = new MenuList();
                mList = new ArrayList<Menu>() ;

                for (int i = 0; i < size; i++) {
                    JSONObject json = array.getJSONObject(i);
                    Menu menu = new Menu() ;

                    menu.setTag(json.getString("tag"));
                    menu.setMenupic(json.getString("menupic"));
                    menu.setMenuname(json.getString("menuname"));
                    
                    mList.add(menu);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            lists.setList(mList);

            return lists;

        }

    }
}
