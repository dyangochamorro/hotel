package com.shine.hotels.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.shine.hotels.center.BaseCenter;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.controller.Events.BackgroundMusicEvent;
import com.shine.hotels.controller.Events.GetToolDataEvent;
import com.shine.hotels.io.model.MusicData;
import com.shine.hotels.io.model.ToolBarData;

import de.greenrobot.event.EventBus;

public class ToolbarController extends BaseController {

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected IntentFilter getFilter() {
		IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.AUDIO_PLAYBACKGROUND);
        filter.addAction(BroadcastAction.MESSAGE_RECV_MESSAGE);
        
		return filter;
	}

	@Override
	protected boolean onUpdateRequest(Request oldOne, Request newOne) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
    protected void onHandle(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.GET_LEFT_TOOLBAR)) {
//            Log.d("shine", "controller Request.Action.GET_LEFT_TOOLBAR");
            BaseCenter center = CenterManager.get(mContext.getApplicationContext())
                    .getCenterByName(CenterManager.CENTER_NAME_TOOLBAR);
            
            center.execute(request);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastAction.MESSAGE_RECV_MESSAGE)) {
//            Log.d("shine", "onReceive MESSAGE_RECV_MESSAGE");
            ToolBarData toolbar = (ToolBarData)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_RESULT);
            
            GetToolDataEvent event = new GetToolDataEvent();
            event.data = toolbar ;
            EventBus.getDefault().post(event);
            
        } else if (action.equals(BroadcastAction.AUDIO_PLAYBACKGROUND)) {
            MusicData data = (MusicData)intent.getSerializableExtra(CenterManager.CENTER_BROADCAST_MUSIC_DATA);
            boolean isPlay = intent.getBooleanExtra(CenterManager.CENTER_BROADCAST_BACKGROUNDMUSIC_STATE, false);
//            Log.i("mp3", "toolbar BroadcastAction.AUDIO_PLAYBACKGROUND play:" + isPlay);
            
            BackgroundMusicEvent event = new BackgroundMusicEvent();
            event.data = data;
            event.isPlaying = isPlay;
            
            EventBus.getDefault().post(event);
        } else if (action.equals(BroadcastAction.AUDIO_STOP_PLAYBACKGROUND)) {
            BackgroundMusicEvent event = new BackgroundMusicEvent();
            event.data = null;
            event.isPlaying = false;
            
            EventBus.getDefault().post(event);
        }
    }

}
