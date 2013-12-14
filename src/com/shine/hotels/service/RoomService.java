
package com.shine.hotels.service;

import java.lang.reflect.Type;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.io.IResponseHandler;
import com.shine.hotels.io.NetEngine;
import com.shine.hotels.io.model.Bill;
import com.shine.hotels.io.model.BillList;
import com.shine.hotels.io.model.Butlerservice;
import com.shine.hotels.io.model.ButlerserviceList;
import com.shine.hotels.io.model.Expresscheckout;
import com.shine.hotels.io.model.ExpresscheckoutList;
import com.shine.hotels.io.model.Memoserv;
import com.shine.hotels.io.model.MemoservList;
import com.shine.hotels.io.model.TestData;
import com.shine.hotels.io.model.UsefulTel;
import com.shine.hotels.io.model.UsefulTelList;
import com.shine.hotels.util.Utils;

public class RoomService extends IntentService {
    private NetEngine mEngine;

    public RoomService() {
        super("RoomService");
        mEngine = new NetEngine();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String action = intent.getAction();
            if (action.equals(Request.Action.GET_PHONE_NO)) {
//                Log.d("shine", "onHandleIntent Request.Action.GET_PHONE_NO");
                UsefulTelList list = mEngine.httpGetRequest(APIManager.API_GET_USEFUL_TEL,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new UsefulTelHandler());
                
                if (null == list) {
                	UsefulTelHandler handler = new UsefulTelHandler();
	                String response = TestData.UsefulTel;
	                list = handler.handleResponse(response);
                }

                Intent intentRes = new Intent(BroadcastAction.ROOMSERVICE_GETUSEFULTEL);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);

            } else if (action.equals(Request.Action.GET_BILLING)) {
//            	Log.d("shine", "onHandleIntent Request.Action.GET_BILLING") ;
            	
            	BillList list = mEngine.httpGetRequest(APIManager.API_GET_BILLQUERY,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new BillqueryHandler());
            	
            	if (null == list) {
            		BillqueryHandler handler = new BillqueryHandler() ;
                	String response = TestData.Billquery ;
                	
                	list = handler.handleResponse(response) ;
            	}
            	
            	Intent intentRes = new Intent(BroadcastAction.ROOMSERVICE_BILLQUERY) ;
            	intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list) ;
            	Utils.sendLocalBrodcast(getApplicationContext(), intentRes) ;
            } else if (action.equals(Request.Action.GET_EXPRESSCHECKOUT)) {
//            	Log.d("shine", "onHandleIntent Request.Action.GET_EXPRESSCHECKOUT") ;
            	
            	ExpresscheckoutList list = mEngine.httpGetRequest(APIManager.API_GET_EXPRESSCHECKOUT,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new ExpresscheckoutHandler());
            	
            	if (null == list) {
            		ExpresscheckoutHandler handler = new ExpresscheckoutHandler() ;
                	String response = TestData.Expresscheckout ;
                	
                	list = handler.handleResponse(response) ;
            	}
            	
            	Intent intentRes = new Intent(BroadcastAction.ROOMSERVICE_EXPRESSCHECKOUT) ;
            	intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list) ;
            	Utils.sendLocalBrodcast(getApplicationContext(), intentRes) ;
            } else if (action.equals(Request.Action.GET_EXPRESSCHECKOUT_CONFIRM)) {
//            	Log.d("shine", "onHandleIntent Request.Action.GET_EXPRESSCHECKOUT_CONFIRM") ;
            	
            	mEngine.httpGetRequest(APIManager.API_GET_EXPRESSCHECKOUT_CONFIRM,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), null);
            	
            } else if (action.equals(Request.Action.HOUSEKEEPER)) { // 管家服务
//            	Log.d("shine", "onHandleIntent Request.Action.HOUSEKEEPER") ;
            	
            	ButlerserviceList list = mEngine.httpGetRequest(APIManager.API_GET_BUTLERSERVICE,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new ButlerserviceHandler());
            	
            	if (null == list) {
            		ButlerserviceHandler handler = new ButlerserviceHandler() ;
                	String response = TestData.Butlerservice ;
                	
                	list = handler.handleResponse(response) ;
            	}
            	
            	Intent intentRes = new Intent(BroadcastAction.ROOMSERVICE_BUTLERSERVICE) ;
            	intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list) ;
            	Utils.sendLocalBrodcast(getApplicationContext(), intentRes) ;
            } else if (action.equals(Request.Action.HOUSEKEEPER_CONFIRM)) {
//            	Log.d("shine", "onHandleIntent Request.Action.HOUSEKEEPER_CONFIRM") ;
            	
            	mEngine.httpGetRequest(APIManager.API_GET_BUTLERSERVICE_CONFIRM,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), null);
            	
            } else if (action.equals(Request.Action.GET_MESSAGE)) {
//            	Log.d("shine", "onHandleIntent Request.Action.GET_MESSAGE") ;
            	
            	MemoservList list = mEngine.httpGetRequest(APIManager.API_GET_MEMOSERV,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new MemoservHandler());
            	
            	if (null == list) {
            		MemoservHandler handler = new MemoservHandler() ;
                	String response = TestData.Memoserv ;
                	
                	list = handler.handleResponse(response) ;
            	}
            	
            	Intent intentRes = new Intent(BroadcastAction.ROOMSERVICE_MEMOSERV) ;
            	intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list) ;
            	Utils.sendLocalBrodcast(getApplicationContext(), intentRes) ;
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class UsefulTelHandler implements IResponseHandler<UsefulTelList> {

        @Override
        public UsefulTelList handleResponse(String response) {
//            Log.d("shine", "UsefulTel response:" + response);
            Gson gson = new Gson();
            
            Type type = new TypeToken<List<UsefulTel>>(){}.getType();
            List<UsefulTel> list = gson.fromJson(response, type);

            UsefulTelList result = new UsefulTelList();
            result.setList(list);
            
            return result;
        }

    }
    
    private class BillqueryHandler implements IResponseHandler<BillList> {
    	
    	@Override
    	public BillList handleResponse(String response) {
//    		Log.d("shine", "Billquery response:"+response) ;
    		Gson gson = new Gson() ;
    		
    		Type type = new TypeToken<List<Bill>>(){}.getType() ;
    		List<Bill> list = gson.fromJson(response, type) ;
    		
    		BillList result = new BillList() ;
    		result.setList(list) ;
    		
    		return result ;
    	}
    }
    
    private class ExpresscheckoutHandler implements IResponseHandler<ExpresscheckoutList> {
    	
    	@Override
    	public ExpresscheckoutList handleResponse(String response) {
//    		Log.d("shine", "Expresscheckout response:"+response) ;
    		Gson gson = new Gson() ;
    		
    		Type type = new TypeToken<List<Expresscheckout>>(){}.getType() ;
    		List<Expresscheckout> list = gson.fromJson(response, type) ;
    		
    		ExpresscheckoutList result = new ExpresscheckoutList() ;
    		result.setList(list) ;
    		
    		return result ;
    	}
    }
    
    private class ButlerserviceHandler implements IResponseHandler<ButlerserviceList> {
    	
    	@Override
    	public ButlerserviceList handleResponse(String response) {
//    		Log.d("shine", "Butlerservice response:"+response) ;
    		Gson gson = new Gson() ;
    		
    		Type type = new TypeToken<List<Butlerservice>>(){}.getType() ;
    		List<Butlerservice> list = gson.fromJson(response, type) ;
    		
    		ButlerserviceList result = new ButlerserviceList() ;
    		result.setList(list) ;
    		
    		return result ;
    	}
    }
    
    private class MemoservHandler implements IResponseHandler<MemoservList> {
    	
    	@Override
    	public MemoservList handleResponse(String response) {
//    		Log.d("shine", "Memoserv response:"+response) ;
    		Gson gson = new Gson() ;
    		
    		Type type = new TypeToken<List<Memoserv>>(){}.getType() ;
    		List<Memoserv> list = gson.fromJson(response, type) ;
    		
    		MemoservList result = new MemoservList() ;
    		result.setList(list) ;
    		
    		return result ;
    	}
    }
}
