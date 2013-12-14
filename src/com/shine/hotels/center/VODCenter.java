package com.shine.hotels.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.shine.hotels.controller.Request;
import com.shine.hotels.io.model.MusicData;
import com.shine.hotels.service.TravelInfoService;
import com.shine.hotels.util.Utils;

public class VODCenter extends BaseCenter {

    public VODCenter(Context context, String name) {
        super(context, name);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void execute(Request request) {
        String action = request.getAction();
        if (action.equals(Request.Action.PLAY_APPRECIATE_MUSIC)) {
            
//            MusicData data = (MusicData)request.getObject();
//            Intent intentRes = new Intent(BroadcastAction.AUDIO_PLAYBACKGROUND);
//            intentRes.putExtra(CenterManager.CENTER_BROADCAST_MUSIC_DATA, data);
//            intentRes.putExtra(CenterManager.CENTER_BROADCAST_BACKGROUNDMUSIC_STATE, true);
//            Utils.sendLocalBrodcast(mContext.getApplicationContext(), intentRes);
        } 
        
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
    
}
