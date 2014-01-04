package com.shine.hotels.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.io.IResponseHandler;
import com.shine.hotels.io.NetEngine;
import com.shine.hotels.io.model.BootInfo;
import com.shine.hotels.io.model.TestData;
import com.shine.hotels.io.model.Welcome;
import com.shine.hotels.io.model.WelcomeList;
import com.shine.hotels.util.Utils;

public class SystemLocalService extends IntentService {
    private NetEngine mEngine;

    public SystemLocalService() {
        super("SystemLocalService");
        mEngine = new NetEngine();
    }

    // private final IBinder mBinder = new SystemLocalBinder();

    // @Override
    // public void onCreate() {
    // // TODO Auto-generated method stub
    // super.onCreate();
    // }
    //
    // @Override
    // public void onDestroy() {
    // // TODO Auto-generated method stub
    // super.onDestroy();
    // }

    // @Override
    // public IBinder onBind(Intent intent) {
    // return mBinder;
    // }

    public void playMusic() {

    }

    public void pauseMusic() {

    }

    public void stopMusic() {

    }

    public class SystemLocalBinder extends Binder {
        public SystemLocalService getService() {
            return SystemLocalService.this;
        }
    }

    String TAG = "WSL";

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String action = intent.getAction();
            if (action.equals(Request.Action.SELECTE_LANG)) {
                // Log.d("shine", "onHandleIntent Request.Action.SELECTE_LANG");
                mEngine.httpGetRequest(APIManager.API_SEL_LANG,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), null);

            } else if (action.equals(Request.Action.BOOT_INFO)) {
                // Log.d("shine", "onHandleIntent Request.Action.BOOT_INFO");

                BootInfo bootInfo = null;
                // 网络连接=====================================
//                for (int i = 0; i < 4; i++) {
//                    bootInfo = mEngine.httpGetRequest(APIManager.API_BOOT_INFO,
//                            intent.getBundleExtra(APIManager.HTTP_PARAM_KEY),
//                            new BootInfoHandler());
//                    if (bootInfo != null) {
//                        break;
//                    } else if (i == 3) {
//                        break;
//                    }
//                    Thread.sleep(5000);
//
//                }
                bootInfo = mEngine.httpGetRequest(APIManager.API_BOOT_INFO,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY),
                        new BootInfoHandler());
                Intent intentRes = new Intent(BroadcastAction.BOOT_INFO);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT,
                        bootInfo);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);

            } else if (action.equals(Request.Action.WELCOME_INIT)) {

                // Log.d("shine", "onHandleIntent Request.Action.WELCOME_INIT");
                WelcomeList list = null;
                // 网络连接=====================================
//                for (int i = 0; i < 4; i++) {
//                    list = mEngine.httpGetRequest(APIManager.API_WELCOME_INIT,
//                            intent.getBundleExtra(APIManager.HTTP_PARAM_KEY),
//                            new WelcomeHandler());
//                    if (list != null) {
//                        break;
//                    } else if (i == 3) {
//                        break;
//                    }
//                    Thread.sleep(5000);
//                }
                list = mEngine.httpGetRequest(APIManager.API_WELCOME_INIT,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY),
                        new WelcomeHandler());
                if (null == list) {
                    WelcomeHandler handler = new WelcomeHandler();
                    String response = TestData.HOMEPAGE;
                    list = handler.handleResponse(response);
                }

                Intent intentRes = new Intent(BroadcastAction.HOTEL_HOMEPAGE);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class BootInfoHandler implements IResponseHandler<BootInfo> {

        @Override
        public BootInfo handleResponse(String response) {
            BootInfo info = new BootInfo();

            // Gson gson = new Gson();
            // Type type = new TypeToken<BootInfo>(){}.getType();
            // info = gson.fromJson(response, type);

            JSONObject json;
            try {
                json = new JSONObject(response);
                info.setType(json.getInt("type"));
                info.setNum(json.getInt("num"));
                info.setTime(json.getString("time"));
                info.setLogo(json.getString("logo"));

                ArrayList<String> urls = new ArrayList<String>();
                JSONArray array = json.getJSONArray("url");
                for (int i = 0; i < array.length(); i++) {
                    urls.add(array.getString(i));
                }

                info.setUrls(urls);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return info;
        }

    }

    private class WelcomeHandler implements IResponseHandler<WelcomeList> {

        @Override
        public WelcomeList handleResponse(String response) {
            WelcomeList lists = null;
            List<Welcome> mList = null;

            JSONArray array;
            try {
//                Log.v("shine", "response:" + response);
                array = new JSONArray(response);
                int size = array.length();
                lists = new WelcomeList();
                mList = new ArrayList<Welcome>();

                for (int i = 0; i < size; i++) {
                    JSONObject json = array.getJSONObject(i);
                    Welcome welcome = new Welcome();

                    welcome.setCustomerName(json.optString("customername"));
                    welcome.setWelcoming(json.optString("welcomeing"));
                    welcome.setLanguageName(json.optString("languagename"));
                    welcome.setLanguagePicNo(json.optString("languagepic_no"));
                    welcome.setLanguagePicYes(json.optString("languagepic_yes"));
                    welcome.setLanguagevalue(json.optString("languagevalue"));
                    welcome.setStatus(json.optInt("status"));

                    mList.add(welcome);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            lists.setmWelcomes(mList);

            return lists;

        }

    }

}
