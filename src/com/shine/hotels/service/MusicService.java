package com.shine.hotels.service;

import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ics.mm.PlayerManager;
import com.ics.mm.PlayerManager.EnumMmInterfaceNotifyType;
import com.ics.mm.PlayerManager.OnPlayerEventListener;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.io.model.MusicData;
import com.shine.hotels.util.Utils;

public class MusicService extends Service {
    private MusicReceiver mReceiver;
    private boolean mIsPlaying = false;
    
    private PlayerManager mPlayerManager;
    private final IBinder mBinder = new MusicBinder();
    
    private List<MusicData> mMusicList;
    private int mCurrentPosition;

    @Override
    public IBinder onBind(Intent intent) {
        if (mIsPlaying) {
            MusicData music = mMusicList.get(mCurrentPosition);
            if (music != null) {
                sendCurPlayBroadcast(music, mCurrentPosition);
            }
        }
        return mBinder;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        mReceiver = new MusicReceiver();
        IntentFilter filter = new IntentFilter(BroadcastAction.MUSIC_STOP_PLAYING);
        registerReceiver(mReceiver, filter);
        
        mPlayerManager = new PlayerManager();
        mPlayerManager.setOnPlayerEventListener(new PlayerManagerListener());
        
    }

    @Override
    public void onDestroy() {
        if (mPlayerManager != null) {
            mPlayerManager.stop();
            mPlayerManager = null;
        }
//        Log.i("shine", "Music Service destroy!!!");
        unregisterReceiver(mReceiver);
        mReceiver = null;
        
        super.onDestroy();
    }
    
    public void playList(List<MusicData> list, int position) {
        mMusicList = list;
//        Log.d("order", "play list size=" + list.size());
        play(position);
    }
    
    public void notifyMusic() {
        if (mIsPlaying) {
            MusicData music = mMusicList.get(mCurrentPosition);
            if (music != null) {
                sendCurPlayBroadcast(music, mCurrentPosition);
            }
        }
    }
    
    private void play(int position) {
        if (mIsPlaying) {
            stop();
        }
        
        if (mMusicList == null || position < 0) return;
        
        MusicData music = mMusicList.get(position);
        if (music != null) {
            String url = music.getMusicurl();
            
            if (mPlayerManager == null) {
                Log.d("audio", "new player");
                mPlayerManager = new PlayerManager();
                mPlayerManager.setOnPlayerEventListener(new PlayerManagerListener());
            }
            Log.d("audio", "setContent url=" + url);
            mPlayerManager.setContentSource(url);
            mPlayerManager.play(url);
            mIsPlaying = true;
//            Log.d("order", "play name:" + music.getMusicname() + " url:" + url);
            
            sendCurPlayBroadcast(music, position);
            
            mCurrentPosition = position;
        }
        
    }
    
    private void stop() {
        if (mPlayerManager != null) {
            mPlayerManager.stop();
        }
        mIsPlaying = false;
    }
    
    public void pause() {
        if (mPlayerManager != null) {
            mPlayerManager.pause();
        }
        mIsPlaying = false;
    }
    
    public void resume() {
        if (mPlayerManager != null) {
            mPlayerManager.resume();
        }
        mIsPlaying = true;
    }
    
    public boolean isPlaying() {
        return mIsPlaying;
    }
    
    private void sendCurPlayBroadcast(MusicData data, int position) {
        Intent intentRes = new Intent(BroadcastAction.AUDIO_PLAYBACKGROUND);
        intentRes.putExtra(CenterManager.CENTER_BROADCAST_MUSIC_DATA, data);
        intentRes.putExtra(CenterManager.CENTER_BROADCAST_MUSIC_POSITION, position);
        intentRes.putExtra(CenterManager.CENTER_BROADCAST_BACKGROUNDMUSIC_STATE, true);
        Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
    }
    
    private void sendStopMusicBroadcast() {
        Intent intentRes = new Intent(BroadcastAction.AUDIO_PLAYBACKGROUND);
        intentRes.putExtra(CenterManager.CENTER_BROADCAST_BACKGROUNDMUSIC_STATE, false);
        Utils.sendLocalBrodcast(getApplicationContext(), intentRes);
    }
    
    public class MusicBinder extends Binder {
        public MusicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicService.this;
        }
    }
    
    public class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(BroadcastAction.MUSIC_STOP_PLAYING)) {
                Log.i("audio", "stop music !!!");
                stop();
                mPlayerManager = null;
                
                sendStopMusicBroadcast();
            }
        }
        
    }
    
    private class PlayerManagerListener implements OnPlayerEventListener {

        @Override
        public boolean onPlayEvent(PlayerManager mgr, EnumMmInterfaceNotifyType notify) {
//            Log.d("order", "type=" + notify);
            switch (notify) {
                case E_MM_INTERFACE_EXIT_OK:
                    if (mMusicList != null) {
                        int newPos = mCurrentPosition + 1;
                        if (newPos >= mMusicList.size()) {
                            newPos = 0;
                        }
//                        Log.d("order", "play next pos=" + newPos);
                        play(newPos);
                    }
                    return true;

                default:
                    break;
            }
            return false;
        }
        
    }

}
