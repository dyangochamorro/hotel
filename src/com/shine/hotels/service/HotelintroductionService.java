
package com.shine.hotels.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.io.IResponseHandler;
import com.shine.hotels.io.NetEngine;
import com.shine.hotels.io.model.HotelintroductionData;
import com.shine.hotels.io.model.HotelintroductionMenu;
import com.shine.hotels.io.model.HotelintroductionMenuList;
import com.shine.hotels.io.model.HotelintroductionShow;
import com.shine.hotels.io.model.HotelintroductionShowPic;
import com.shine.hotels.io.model.TestData;
import com.shine.hotels.util.Utils;

public class HotelintroductionService extends IntentService {
    private NetEngine mEngine;

    public HotelintroductionService() {
        super("Hotelintroduction");
        mEngine = new NetEngine();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String action = intent.getAction();

            if (action.equals(Request.Action.GET_HOTELINTRODUCTION_MENU)) {
//                Log.d("shine", "onHandleIntent Request.Action.GET_HOTELINTRODUCTION_MENU");
                // 网络请求
                HotelintroductionMenuList list = mEngine.httpGetRequest(
                        											APIManager.API_GET_HOTELINTRODUCTION_MENU,
                        											intent.getBundleExtra(APIManager.HTTP_PARAM_KEY),
                        											new HotelintroductionMenuHandler());
                // 本地测试
                if (null == list) {
                	HotelintroductionMenuHandler handler = new HotelintroductionMenuHandler();
                    String response = TestData.HotelintroductionMenu;
                   
                    list = handler.handleResponse(response);
                }

                Intent intentRes = new Intent(BroadcastAction.HOTEL_INTRODUCTION_MENU);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } else if (action.equals(Request.Action.GET_HOTELINTRODUCTION_SHOW)) {
//                Log.d("shine", "onHandleIntent Request.Action.GET_HOTELINTRODUCTION_SHOW");
                // 网络请求
                HotelintroductionShow showData = mEngine.httpGetRequest(
										                        APIManager.API_GET_HOTELINTRODUCTION_SHOW,
										                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY),
										                        new HotelintroductionShowHandler());
                // 本地测试
                if (null == showData) {
                	HotelintroductionShowHandler handler = new HotelintroductionShowHandler();
                	String response = TestData.HotelintroductionShow;
                
                	showData = handler.handleResponse(response);
                }

                Intent intentRes = new Intent(BroadcastAction.HOTEL_INTRODUCTION_SHOW);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, showData);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class HotelintroductionMenuHandler implements
            IResponseHandler<HotelintroductionMenuList> {

        @Override
        public HotelintroductionMenuList handleResponse(String response) {
            HotelintroductionMenuList lists = null;
            List<HotelintroductionMenu> mList = null;

            JSONArray array;
            try {
                array = new JSONArray(response);
                int size = array.length();
                lists = new HotelintroductionMenuList();
                mList = new ArrayList<HotelintroductionMenu>();

                for (int i = 0; i < size; i++) {
                    JSONObject json = array.getJSONObject(i);
                    HotelintroductionMenu menu = new HotelintroductionMenu();

                    menu.setSortingname(json.getString("sortingname"));
                    menu.setSortingpic(json.getString("sortingpic"));
                    menu.setType(json.getInt("type"));

                    ArrayList<HotelintroductionData> datas = new ArrayList<HotelintroductionData>();
                    JSONArray subArray = json.optJSONArray("data");
                    for (int j = 0; j < subArray.length(); j++) {
                        JSONObject jdata = subArray.optJSONObject(j);
                        if (jdata != null) {
                            HotelintroductionData data = new HotelintroductionData();
                            data.setDataContent(jdata.optString("dataContent"));
                            data.setDataId(jdata.optInt("dataId"));

                            datas.add(data);
                        }
                    }

                    menu.setData(datas);

                    mList.add(menu);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            lists.setList(mList);

            return lists;

        }

    }

    private class HotelintroductionShowHandler implements IResponseHandler<HotelintroductionShow> {

        @Override
        public HotelintroductionShow handleResponse(String response) {
            HotelintroductionShow showData = null;
            JSONObject json;
            try {
                json = new JSONObject(response);

                showData = new HotelintroductionShow();

                showData.setTitle(json.getString("title"));
                showData.setContent(json.getString("content"));
                showData.setTvUrl(json.getString("tvUrl"));
                showData.setFlag(json.getInt("flag"));
                showData.setPic(json.getString("pic"));

                ArrayList<String> pics = new ArrayList<String>();
                JSONArray subArray = json.optJSONArray("pics");
                if (subArray != null) {
                    for (int j = 0; j < subArray.length(); j++) {
                        String pic = subArray.getString(j);
                        
                        pics.add(pic);
                    }
                }

                showData.setPics(pics);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return showData;

        }

    }

}
