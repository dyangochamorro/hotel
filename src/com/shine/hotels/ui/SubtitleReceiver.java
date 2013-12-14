package com.shine.hotels.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.shine.hotels.io.model.Subtitle;

public class SubtitleReceiver extends BroadcastReceiver {
    private Handler mHandler;
    
    public SubtitleReceiver(Handler handler) {
        mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (mHandler != null) {
            boolean show = intent.getBooleanExtra(HostActivity.SHOW_SUBTEXT_KEY, false);
//            Log.i("shine", "subtitle receiver!!! " + show);
            mHandler.sendEmptyMessage(show ? HostActivity.SHOW_SUBTEXT : HostActivity.HIDE_SUBTEXT);
                
        }
    }

}
