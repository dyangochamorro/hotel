
package com.shine.hotels.service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.io.IResponseHandler;
import com.shine.hotels.io.NetEngine;
import com.shine.hotels.io.model.CarRental;
import com.shine.hotels.io.model.CarRentalList;
import com.shine.hotels.io.model.Flight;
import com.shine.hotels.io.model.FlightList;
import com.shine.hotels.io.model.Train;
import com.shine.hotels.io.model.TrainList;
import com.shine.hotels.io.model.Weather;
import com.shine.hotels.io.model.WeatherList;
import com.shine.hotels.io.model.WorldTime;
import com.shine.hotels.io.model.WorldTimeList;
import com.shine.hotels.util.Utils;

public class TravelInfoService extends IntentService {
    private NetEngine mEngine;

    public TravelInfoService() {
        super("TravelInfo");
        mEngine = new NetEngine();
    }

    @Override
    protected void onHandleIntent(Intent requestIntent) {
        try {
            String action = requestIntent.getAction();
            if (action.equals(Request.Action.GET_WEATHERS)) {
//                Log.d("shine", "onHandleIntent Request.Action.GET_WEATHERS");
                WeatherList list = mEngine.httpGetRequest(APIManager.API_GET_WEATHER,
                        requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new WeatherHandler());
                
//                String response = "";
                Bundle param = requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY);
                String city = param.getString("city");
//                if (city.equals("1")) {
//                    response = TestData.Weather;
//                } else {
//                    response = TestData.SingleWeather;
//                }
                
//                if (null == list) {
//                    WeatherHandler handler = new WeatherHandler();
//                    list = handler.handleResponse(response);
//                }

                Intent intentRes = new Intent(BroadcastAction.TRAVELINFO_GETWEATHER);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_SRC, city);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);

            } else if (action.equals(Request.Action.GET_WEATHERS_BACK)) {
//            	Log.d("shine", "onHandleIntent Request.Action.GET_WEATHERS_BACK") ;
            	
            	mEngine.httpGetRequest(APIManager.API_GET_WEATHER_BACK,
            			requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), null);
            	
            } else if (action.equals(Request.Action.GET_WORLDTIME)) {
//                Log.d("shine", "onHandleIntent Request.Action.GET_WORLDTIME");
                WorldTimeList list = mEngine.httpGetRequest(APIManager.API_GET_WORLDTIME,
                        requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new WorldTimeHandler());
                
                Bundle param = requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY);
                String city = param.getString("city");
                
//                if (null == list) {
//                	WorldTimeHandler handler = new WorldTimeHandler();
//                    String response = TestData.WorldTime;
//                    list = handler.handleResponse(response);
//                }
                
                Intent intentRes = new Intent(BroadcastAction.TRAVELINFO_GETWORLDTIME);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_SRC, city);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } else if (action.equals(Request.Action.GET_WORLDTIME_BACK)) {
//            	Log.d("shine", "onHandleIntent Request.Action.GET_WORLDTIME_BACK") ;
            	
            	mEngine.httpGetRequest(APIManager.API_GET_WORLDTIME_BACK,
            			requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), null);
            	
            } else if (action.equals(Request.Action.GET_CAR)) {
//            	Log.d("shine", "onHandleIntent Request.Action.GET_CAR");
            	CarRentalList list = mEngine.httpGetRequest(APIManager.API_GET_CARRENTAL,
                        requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new CarrentalHandler());
                
//            	if (null == list) {
//            		CarrentalHandler handler = new CarrentalHandler() ;
//                	String response = TestData.Carrental ;
//                	
//                	list = handler.handleResponse(response) ;
//            	}
            	
            	Intent intentRes = new Intent(BroadcastAction.TRAVELINFO_GETCAR);
            	intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } else if (action.equals(Request.Action.GET_SCHEDULE)) {
//            	Log.d("shine", "onHandleIntent Request.Action.GET_SCHEDULE");
            	TrainList list = mEngine.httpGetRequest(APIManager.API_GET_TRAININFOMATION,
                        requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new TrainHandler());
                
//            	if (null == list) {
//            		TrainHandler handler = new TrainHandler() ;
//                	String response = TestData.Traininfomation ;
//                	
//                	list = handler.handleResponse(response) ;
//            	}
            	
            	Intent intentRes = new Intent(BroadcastAction.TRAVELINFO_GETSCHEDULE);
            	intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } else if (action.equals(Request.Action.GET_FLIGHTINFO)) {
//                Log.d("shine", "onHandleIntent Request.Action.GET_FLIGHTINFO");
                FlightList[] list = mEngine.httpGetRequest(APIManager.API_GET_FLIGHTINFOMATION,
                        requestIntent.getBundleExtra(APIManager.HTTP_PARAM_KEY), new FlightInfoHandler());
                
//                if (null == list) {
//                	FlightInfoHandler handler = new FlightInfoHandler() ;
//                    String response = TestData.FlightInfo;
//                    
//                    list = handler.handleResponse(response) ;
//                }
                
                Intent intentRes = new Intent(BroadcastAction.TRAVELINFO_GETFLIGHTINFO);
                intentRes.putExtra(CenterManager.CENTER_BROADCAST_RESULT, list);
                Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
            } 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class WeatherHandler implements IResponseHandler<WeatherList> {

        @Override
        public WeatherList handleResponse(String response) {
            //Log.d("shine", "response:" + response);
            Gson gson = new Gson();
            try {
            	Type type = new TypeToken<List<Weather>>(){}.getType();
                List<Weather> list = gson.fromJson(response, type);
                
                // 转化code为城市名称
//                for (Weather w : list) {
//                    String code = w.getCode();
//                    w.setCity(code/*Utils.getCityFromCode(TravelInfoService.this, code)*/);
//                }

                WeatherList weathers = new WeatherList();
                weathers.setWeathers(list);
                
                return weathers;
			} catch (Exception e) {
				e.printStackTrace() ;
				return null ;
			}
            
        }

    }
    
    private class WorldTimeHandler implements IResponseHandler<WorldTimeList> {

        @Override
        public WorldTimeList handleResponse(String response) {
            //Log.d("shine", "response:" + response);
            Gson gson = new Gson();
            try {
	            Type type = new TypeToken<List<WorldTime>>(){}.getType();
	            List<WorldTime> list = gson.fromJson(response, type);
	            // 转化code为城市名称
	//            for (WorldTime t : list) {
	//                String code = t.getCode();
	//                t.setCity(Utils.getCityFromCode(TravelInfoService.this, code));
	//            }
	
	            WorldTimeList times = new WorldTimeList();
	            times.setWorldTimes(list);
	            
	            return times;
            } catch (Exception e) {
				e.printStackTrace() ;
				return null ;
			}
        }

    }
    
    private class CarrentalHandler implements IResponseHandler<CarRentalList> {

        @Override
        public CarRentalList handleResponse(String response) {
            //Log.d("shine", "response:" + response);
            Gson gson = new Gson();
            
            try {
	            Type type = new TypeToken<List<CarRental>>(){}.getType();
	            List<CarRental> list = gson.fromJson(response, type);
	
	            CarRentalList carRental = new CarRentalList();
	            carRental.setList(list);
	            
	            return carRental;
            } catch (Exception e) {
				e.printStackTrace() ;
				return null ;
			}
        }

    }
    
    private class TrainHandler implements IResponseHandler<TrainList> {

        @Override
        public TrainList handleResponse(String response) {
            Gson gson = new Gson();
            try {
	            Type type = new TypeToken<List<Train>>(){}.getType();
	            List<Train> list = gson.fromJson(response, type);
	
	            TrainList trainList = new TrainList();
	            trainList.setTrains(list) ;
	            
	            return trainList;
            } catch (Exception e) {
				e.printStackTrace() ;
				return null ;
			}
        }

    }
    
    
    private class FlightInfoHandler implements IResponseHandler<FlightList[]> {

        @Override
        public FlightList[] handleResponse(String response) {
            FlightList[] lists = null;
            
            JSONArray array;
            try {
                array = new JSONArray(response);
                int size = array.length();
                lists = new FlightList[size];
                for(int i = 0; i < size; i++) {
                    JSONObject json = array.getJSONObject(i);
                    lists[i] = new FlightList();
                    lists[i].setCategory(json.getString("category"));
                    
                    ArrayList<Flight> flist = new ArrayList<Flight>();
                    JSONArray subArray = json.optJSONArray("data");
                    for (int j = 0; j < subArray.length(); j++) {
                        JSONObject jFlight = subArray.optJSONObject(j);
                        if (jFlight != null) {
                            Flight f = new Flight();
                            f.setAirline(jFlight.optString("airline"));
                            f.setNumber(jFlight.optString("number"));
                            f.setExpectedDepart(jFlight.optString("expectedDepart"));
                            f.setActualDepart(jFlight.optString("actualDepart"));
                            f.setDeparture(jFlight.optString("departure"));
                            f.setDestinationTerminal(jFlight.optString("departureTerminal"));
                            f.setExpectedArrive(jFlight.optString("expectedArrive"));
                            f.setActualArrive(jFlight.optString("actualArrive"));
                            f.setDestination(jFlight.optString("destination"));
                            f.setDestinationTerminal(jFlight.optString("destinationTerminal"));
                            f.setStatus(jFlight.optString("status"));
                            
                            flist.add(f);
                        }
                    }
                    
                    lists[i].setFlights(flist);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
            return lists;
        }

    }
}
