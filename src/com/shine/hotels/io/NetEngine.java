
package com.shine.hotels.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import android.os.Bundle;
import android.util.Log;

import com.shine.hotels.HotelsApplication;
import com.squareup.okhttp.OkHttpClient;

public class NetEngine {
    private OkHttpClient mClient = new OkHttpClient();

    public <T> T httpGetRequest(String api, Bundle param, IResponseHandler<T> handler) throws IOException {
        HttpURLConnection connection = null ;
        InputStream in = null;
        try {
        	String request = buildRequest(api, param);
        	Log.v("shine", "request:" + request);
        	connection = mClient.open(new URL(request));
        	connection.setConnectTimeout(10000) ;
            // Read the response.
            in = connection.getInputStream();
            byte[] response = readFully(in);
            String string = new String(response, "UTF-8");

            if (null == handler)
            	return null ;
            return handler.handleResponse(string);

        } catch(IOException ioe) {
        	ioe.printStackTrace() ;
        	HotelsApplication.showText();
        	return null ;
        } finally {
        	try {
                if (in != null)
                    in.close();
			} catch (IOException e) {
				e.printStackTrace() ;
			}
        }
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
