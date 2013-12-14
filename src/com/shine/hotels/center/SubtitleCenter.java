package com.shine.hotels.center;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shine.hotels.controller.Request;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.service.SubtitleService;

public class SubtitleCenter extends BaseCenter {
	
	private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
	private UpdateProgressBarTask mUpdateTask;

    public SubtitleCenter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {
    	mScheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(2);
    }

    @Override
    public void execute(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.GET_SUBTITLE)) {
//            Log.d("shine", "execute Request.Action.GET_SUBTITLE");
            
            mUpdateTask = new UpdateProgressBarTask(request, action) ;
            mScheduledThreadPoolExecutor.scheduleAtFixedRate(mUpdateTask, 1, 300, TimeUnit.SECONDS);
        } 
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    	if (mScheduledThreadPoolExecutor != null) {
            mScheduledThreadPoolExecutor.remove(mUpdateTask);
            mScheduledThreadPoolExecutor.shutdown();
            mScheduledThreadPoolExecutor = null;
        }
    }
    
    private class UpdateProgressBarTask implements Runnable {
    	Request request ;
    	String action ;
    	
    	public UpdateProgressBarTask(Request request, String action) {
    		this.request = request ;
    		this.action = action ;
    	}

        @Override
        public void run() {
        	Bundle param = request.getParam();
            Intent intent = new Intent(mContext, SubtitleService.class);
            intent.setAction(action);
            intent.putExtra(APIManager.HTTP_PARAM_KEY, param);
            mContext.startService(intent);
        }
        
    }
    
}
