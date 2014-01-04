
package com.shine.hotels.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

public class NetEngine {
//    private int mRequestCnt;
//    private String mApi = "";
//    private boolean mIsTimeOut = false;
    private OkHttpClient mClient = new OkHttpClient();

    public <T> T httpGetRequest(String api, Bundle param, IResponseHandler<T> handler) throws IOException {
//        if (!mApi.equals(api)) {
//            mRequestCnt = 0;
//            mApi = api;
//        }
//        try {
//            if (mRequestCnt >= 10) {
//                return null;
//            } else {
//                mRequestCnt++;
//            }
            
        	String request = buildRequest(api, param);
        	
        	byte[] response = null;
        	for (int i = 0; i < 10; i++) {
        	    Log.v("shine", "request:" + request + " cnt=" + i);
        	    long start = System.currentTimeMillis();
        	    response = doRequest(request);
        	    if (response != null) {
        	        break;
        	    } else {
        	        long dur = System.currentTimeMillis() - start;
                    if (dur < 5000) {
                        try {
                            Thread.sleep(5000 - dur);
                        } catch (InterruptedException e) {
//                            e.printStackTrace();
                        }
                    }
        	    }
        	}
        	
            if (null != handler && response != null) {
                String string = new String(response, "UTF-8");
                return handler.handleResponse(string);
            }

//        } catch(IOException ioe) {
//        } 
        return null;
    }
    
    private byte[] doRequest(String request) {
        HttpURLConnection connection = null;
        InputStream in = null;
        byte[] response = null;

        try {
            connection = mClient.open(new URL(request));
            // connection.setConnectTimeout(30000) ;
            // connection.setReadTimeout(30000);
            // Read the response.
            in = connection.getInputStream();
            response = readFully(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            Log.v("shine", "IOException!");
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    private String buildRequest(String api, Bundle param) {
        StringBuilder sb = new StringBuilder(APIManager.HOST).append(api);
        if (param != null) {
            Set<String> keys = param.keySet();
            if (!keys.isEmpty()) {
                sb.append("?");
                
                Iterator<String> iterator = keys.iterator();
                String key = iterator.next();
                sb.append(key).append("=").append(param.get(key));

                while (iterator.hasNext()) {
                    sb.append("&");
                    key = (String)iterator.next();
                    sb.append(key).append("=").append(param.get(key));
                }
            }
        }

        return sb.toString();
    }

    private byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1;) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }
}
