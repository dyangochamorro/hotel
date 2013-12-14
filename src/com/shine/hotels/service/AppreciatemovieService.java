
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
import com.shine.hotels.io.model.Appreciatemovie;
import com.shine.hotels.io.model.AppreciatemovieList;
import com.shine.hotels.io.model.AppreciatemovieShow;
import com.shine.hotels.io.model.MovieData;
import com.shine.hotels.io.model.PlayintromsgData;
import com.shine.hotels.io.model.TestData;
import com.shine.hotels.util.Utils;

public class AppreciatemovieService extends IntentService {
    private NetEngine mEngine;

    public AppreciatemovieService() {
        super("Appreciatemovie");
        mEngine = new NetEngine();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String action = intent.getAction();
            
            if (action.equals(Request.Action.GET_APPRECIATE_MOVIE)) {
//            	Log.d("shine", "onHandleIntent Request.Action.GET_APPRECIATE_MOVIE");
            	// 网络请求
            	AppreciatemovieList list = mEngine.httpGetRequest(APIManager.API_GET_APPRECIATE_MOVIE,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new AppreciatemovieHandler());
                // 本地测试
            	if (null == list) {
	            	AppreciatemovieHandler handler = new AppreciatemovieHandler() ;
	            	String response = TestData.Appreciatemovie ;
	            	
	            	list = handler.handleResponse(response) ;
            	}
            	
            	Intent intentRes = new Intent(BroadcastAction.APPRECIATE_MOVIE);
            	intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } else if (action.equals(Request.Action.GET_APPRECIATE_MOVIE_SHOW)) {
            	Log.d("pay", "onHandleIntent Request.Action.GET_APPRECIATE_MOVIE_SHOW");
            	// 网络请求
            	AppreciatemovieShow showData = mEngine.httpGetRequest(APIManager.API_GET_APPRECIATE_MOVIE_SHOW,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new AppreciatemovieShowHandler());
                // 本地测试
            	if (null == showData) {
            		AppreciatemovieShowHandler handler = new AppreciatemovieShowHandler() ;
                	String response = TestData.AppreciatemovieShow ;
                	
                	showData = handler.handleResponse(response) ;
            	} else {
            	    List<PlayintromsgData> list = showData.getData();
                    if (list != null) {
                        for (PlayintromsgData data : list) {
                            Log.d("pay", "data id:" + data.getId() + " type:" + data.getType() + " title:" + data.getIntrotitle());
                            
                        }
                    } else {
//                        Log.d("pay", "showData list is null!!!");
                    }
            	}
            	
            	Intent intentRes = new Intent(BroadcastAction.APPRECIATE_MOVIE_SHOW);
            	intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, showData);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } else if (action.equals(Request.Action.GET_PAY_BACK)) {
//                Log.d("shine", "onHandleIntent Request.Action.GET_PAY_BACK");
                // 网络请求
                mEngine.httpGetRequest(APIManager.API_GET_APPRECIATE_MOVIE_PAY,
                        intent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new AppreciatemovieShowHandler());
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class AppreciatemovieHandler implements IResponseHandler<AppreciatemovieList> {

        @Override
        public AppreciatemovieList handleResponse(String response) {
        	AppreciatemovieList lists = null ;
        	List<Appreciatemovie> aList = null ;
            
            JSONArray array;
            try {
                array = new JSONArray(response);
                int size = array.length();
                lists = new AppreciatemovieList() ;
                aList = new ArrayList<Appreciatemovie>() ;
                
                for(int i = 0; i < size; i++) {
                    JSONObject json = array.getJSONObject(i);
                    Appreciatemovie movie = new Appreciatemovie() ;
                    
                    movie.setMovietypename(json.getString("movietypename"));
                    
                    ArrayList<MovieData> datas = new ArrayList<MovieData>();
                    JSONArray subArray = json.optJSONArray("movies");
                    for (int j = 0; j < subArray.length(); j++) {
                        JSONObject jdata = subArray.optJSONObject(j);
                        if (jdata != null) {
                        	MovieData data = new MovieData();
                        	data.setMoviename(jdata.optString("moviename"));
                        	data.setMoviepic(jdata.optString("moviepic")) ;
                        	data.setMovietype(jdata.optInt("type"));
//                        	data.setMovieId(jdata.optInt("movieId"));
                        	JSONArray jids = jdata.getJSONArray("ids");
                        	final int length = jids.length();
                        	int[] ids = new int[length];
                        	for (int k = 0; k < length; k++) {
                        	    ids[k] = jids.getInt(k);
                        	}
                        	data.setMovieIds(ids);
                            
                        	datas.add(data);
                        }
                    }
                    
                    movie.setData(datas) ;
                    
                    aList.add(movie) ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            lists.setList(aList) ;
            
            return lists ;

        }

    }
    
    private class AppreciatemovieShowHandler implements IResponseHandler<AppreciatemovieShow> {

        @Override
        public AppreciatemovieShow handleResponse(String response) {
        	AppreciatemovieShow showData = null ;
            JSONObject json ;
            try {
//                Log.d("shine", "show:" + response);
                    json = new JSONObject(response) ;
                    
                    showData = new AppreciatemovieShow() ;
                    
                    showData.setMovieposter(json.getString("movieposter"));
                    showData.setMoviename(json.getString("moviename"));
                    showData.setDirector(json.getString("director"));
                    showData.setProtagonist(json.getString("protagonist"));
                    showData.setRunningtime(json.getString("runningtime"));
                    showData.setIntro(json.getString("intro"));
                    JSONArray array = json.getJSONArray("movieplayurl");
                    List<String> urls = new ArrayList<String>();
                    for (int i = 0; i < array.length(); i++) {
                        String url = array.getString(i);
                        urls.add(url);
                    }
                    showData.setMovieplayurl(urls);
                    showData.setMoviepreviewurl(json.getString("moviepreviewurl"));
                    
                    JSONArray subArray = json.optJSONArray("playintromsgs");
                    if (subArray != null) {
                        ArrayList<PlayintromsgData> datas = new ArrayList<PlayintromsgData>();
                        for (int j = 0; j < subArray.length(); j++) {
                            JSONObject jdata = subArray.optJSONObject(j);
                            if (jdata != null) {
                            	PlayintromsgData data = new PlayintromsgData();
                            	data.setIntrotitle(jdata.optString("introtitle")) ;
                            	data.setIntrocontent(jdata.optString("introcontent")) ;
                            	data.setType(jdata.optInt("type"));
                                
                            	datas.add(data);
                            }
                        }
                        showData.setData(datas) ;
                    }
                    
                    
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            
            return showData ;

        }

    }
    
}
