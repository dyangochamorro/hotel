package com.shine.hotels.ui;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ics.mm.PlayerManager;
import com.ics.mm.PlayerManager.EnumMmInterfaceNotifyType;
import com.ics.mm.PlayerManager.OnPlayerEventListener;
import com.mstar.tv.service.skin.AudioSkin;
import com.shine.hotels.R;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetToolDataEvent;
import com.shine.hotels.controller.Events.SubtitleEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.SubtitleController;
import com.shine.hotels.io.model.Subtitle;
import com.shine.hotels.io.model.ToolBarData;
import com.shine.hotels.ui.widget.ExitDialog;
import com.shine.hotels.util.Utils;

import de.greenrobot.event.EventBus;

public class FullScreenPlayActivity extends Activity implements SurfaceHolder.Callback,
        OnPlayerEventListener, OnClickListener {
    public static final String INTENT_KEY_MOVIE_URL = "movie_url";
    public static final String INTENT_KEY_PLAY_TYPE = "play_type";
    
    public static final int TYPE_PREVIEW = 1;
    public static final int TYPE_PLAY = 2;
    public static final int TYPE_BOOT = 3;
    
    private static final int FORWARD_OFFSET = 8;
    
    private SurfaceView mSurfaceview = null;
    private ImageButton mPlayBtn;
    private ImageButton mBackBtn;
    private ImageButton mForward;
//    private TextView mPlayTimeTv;
    private TextView mTotalTimeTv;
    private ImageView mMuteIv;
    private ImageView mMsgIv;
    private LinearLayout mExitLayout;
    private Button mConfirm;
    private Button mCancel;
    
    private LinearLayout mProcessLayout;
    private ProgressBar mProgressBar;
    
    private PlayerManager mPlayerManager;
    private String mMovieUrl = "";
    private Handler mHandler = new Handler();
    private int mTotal;
    private int mCurrentTime;
//    private int mSeekTo;
    private AudioSkin mAudioSkin;
    private int mVol;
    
    private boolean mIsStartPlay;
    int mType;
    
    private enum STATE {
        PLAYING,
        FORWARDING,
        BACKING,
        PAUSE
    }
    
    private STATE mState = STATE.PLAYING;
    
    private SubtitleController mController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_play_activity);
        
        Intent intent = getIntent();
        mMovieUrl = intent.getStringExtra(INTENT_KEY_MOVIE_URL);
        mType = intent.getIntExtra(INTENT_KEY_PLAY_TYPE, TYPE_PREVIEW);
        if (mType == TYPE_PREVIEW) {
            mHandler.postDelayed(new Runnable() {
                
                @Override
                public void run() {
                    finish();
                }
            }, 3 * 60 * 1000);
        }
        
        mProcessLayout = (LinearLayout)findViewById(R.id.process_layout);
        
        mSurfaceview = (SurfaceView)findViewById(R.id.play_layer);
        mSurfaceview.getHolder().addCallback(this);
        mPlayBtn = (ImageButton)findViewById(R.id.play_btn);
        mPlayBtn.requestFocus();
        mPlayBtn.setOnClickListener(this);
        mForward = (ImageButton)findViewById(R.id.forward_btn);
        mForward.setOnClickListener(this);
        mBackBtn = (ImageButton)findViewById(R.id.back_btn);
        mBackBtn.setOnClickListener(this);
        mTotalTimeTv = (TextView)findViewById(R.id.total_time);
        mMuteIv = (ImageView)findViewById(R.id.mute_iv);
        mMsgIv = (ImageView)findViewById(R.id.message);
        
        mProgressBar = (ProgressBar)findViewById(R.id.vol_progress);
        
        mPlayerManager = new PlayerManager();
        mPlayerManager.setOnPlayerEventListener(this);
        
        mAudioSkin = new AudioSkin(this);
        mAudioSkin.connect(null);
        if (mAudioSkin.GetMuteFlag()) {
            mAudioSkin.setMuteFlag(false);
        }
        mVol = 20;
        mAudioSkin.setVolume(mVol);
        
        mPopupText = (TextView)findViewById(R.id.pop_up_txt);
        mSubtitleReceiver = new SubtitleReceiver(myHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.SUBTITLE);
        registerReceiver(mSubtitleReceiver, filter);
        
        EventBus.getDefault().register(this);
        
        mController = (SubtitleController)ControllerManager.newController(getApplicationContext(), SubtitleController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_SUBTITLE).getResult();
        mController.handle(request);
        Request request2 = builder.obtain(Request.Action.GET_LEFT_TOOLBAR).getResult();
        mController.handle(request2);
    }
    
    @Override
    protected void onStart() {
        super.onStart();

//        showInfoBox();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mSubtitleReceiver);
        
        EventBus.getDefault().unregister(this);
        
        if (mPlayerManager != null) {
            mPlayerManager.stop();
            mPlayerManager = null;
        }
        mHandler.removeCallbacks(mGetTime);
        super.onDestroy();
    }

    @Override
    public boolean onPlayEvent(PlayerManager mgr, EnumMmInterfaceNotifyType notify) {
        switch (notify) {
            case E_MM_INTERFACE_START_PLAY:
                Log.d("order", "E_MM_INTERFACE_START_PLAY");
                mTotal = mPlayerManager.getDuration();
                mIsStartPlay = true;
                
                mHandler.postDelayed(new Runnable() {
                    
                    @Override
                    public void run() {
                        showInfoBox();
                    }
                }, 1000);
                
                break;
                
            case E_MM_INTERFACE_EXIT_OK:
                Log.d("order", "E_MM_INTERFACE_EXIT_OK");
                if (mType == TYPE_BOOT) {
                    restart();
                } else {
                    stopVideo();
                    finish();
                }
                break;
                
            case E_MM_INTERFACE_SUFFICIENT_DATA:
                Log.d("order", "E_MM_INTERFACE_SUFFICIENT_DATA state=" + mState);
                if (mState == STATE.BACKING || mState == STATE.FORWARDING) {
                    mState = STATE.PLAYING;
                    updateBtn();
                    
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showInfoBox();
                        }
                    }, 2 * 1000);
                }
                break;
            case E_MM_INTERFACE_EXIT_ERROR:
                break;
            case E_MM_INTERFACE_INSUFFICIENT_DATA:
//                stopVideo();
//                finish();
              break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v("order", "surfaceCreated...");
        playVideo(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Log.v("order", "surfaceDestroyed...");
        stopVideo();
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.play_btn:
                clickPlayBtn();
                break;
                
            case R.id.forward_btn:
                forward();
                updateBtn();
                break;
                
            case R.id.back_btn:
                back();
                updateBtn();
                break;
                
            case R.id.confirm:
                finish();
                break;
                
            case R.id.cancel:
                mExitLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
        
    }
    
    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
//        Log.v("order", "keycode=" + keyCode);
        
        if (mType == TYPE_BOOT) {
            if (mState == STATE.PAUSE || mState == STATE.PLAYING) {
                finish();
            } 
            return true;
        }
        
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_BACK:
//                finish();
                showExitDialog();
                return true;
                
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (playboxshowing) {
                    return false;
                } else {
                    showInfoBox();
                    return true;
                }
                
            case 36:
                volumeDown();
                return true;
                
            case 35:
                volumeUp();
                return true;
                
            case 37:
                if (!playboxshowing) {
                    showInfoBox();
                }
                volumeMute();
                return true;

            default:
                showInfoBox();
                break;
        }
        return true;
    }
    
    private void volumeUp() {
        showInfoBox();
        
        if (mAudioSkin.GetMuteFlag()) {
            mAudioSkin.setMuteFlag(false);
            mMuteIv.setImageResource(R.drawable.vol_sound);
        }
        
        mVol += 5;
        mVol = mVol < 100 ? mVol : 100;
        mAudioSkin.setVolume(mVol);
        mProgressBar.setProgress(mVol);
    }
    
    private void volumeDown() {
        showInfoBox();
        
        if (mAudioSkin.GetMuteFlag()) {
            mAudioSkin.setMuteFlag(false);
            mMuteIv.setImageResource(R.drawable.vol_sound);
        }
        
        mVol -= 5;
        mVol = mVol > 0 ? mVol : 0;
        mAudioSkin.setVolume(mVol);
        mProgressBar.setProgress(mVol);
    }
    
    private void volumeMute() {
        if (mAudioSkin.GetMuteFlag()) {
            mAudioSkin.setMuteFlag(false);
            mMuteIv.setImageResource(R.drawable.vol_sound);
        } else {
            mAudioSkin.setMuteFlag(true);
            mMuteIv.setImageResource(R.drawable.vol_mute);
        }
    }
    
    private void updateBtn() {
        if (mState == STATE.PLAYING) {
            mPlayBtn.setBackgroundResource(R.drawable.pause_btn);
        } else {
            mPlayBtn.setBackgroundResource(R.drawable.play_btn);
        }
    }
    
    private void forward() {
        if (mState == STATE.PLAYING) {
            mCurrentTime = mPlayerManager.getPlayerTime();
//            mSeekTo = mCurrentTime;
        }

        mPlayerManager.setPlayMode(FORWARD_OFFSET);
        mState = STATE.FORWARDING;

    }
    
    private void back() {

        if (mState == STATE.PLAYING) {
            mCurrentTime = mPlayerManager.getPlayerTime();
//            mSeekTo = mCurrentTime;
        }

        mPlayerManager.setPlayMode(-FORWARD_OFFSET);
        mState = STATE.BACKING;

    }
    
    private void seekToVideo(int seekTo) {
        pauseVideo();
        
        mPlayerManager.setPlayMode(FORWARD_OFFSET);
        
        StringBuilder time = new StringBuilder(Utils.formatTime(seekTo));
        time.append("/");
        time.append(Utils.formatTime(mTotal));
    }
    
    private void playVideo(SurfaceHolder holder) {
        String url = mMovieUrl;
        if (mPlayerManager != null) {
            mPlayerManager.setContentSource(url);
            mPlayerManager.setDisplay(holder);
            mPlayerManager.play(url);
            
            Log.d("order", "player play url = " + url);
            
            mState = STATE.PLAYING;
        }
    }
    
    private void restart() {
        if (mPlayerManager == null) return;
        
        mPlayerManager.stop();
        Log.d("order", "player restart play url = " + mMovieUrl);
        
        mPlayerManager.setContentSource(mMovieUrl);
        mPlayerManager.play(mMovieUrl);
    }
    
    private void stopVideo() {
        if (mPlayerManager == null) return;
        
        mPlayerManager.stop();
        mPlayerManager = null;
    }
    
    private void clickPlayBtn() {
        if (mType == TYPE_BOOT) return;
        
        if (mState == STATE.FORWARDING ||
                mState == STATE.BACKING) {
            pauseVideo();
            resumeVideo();
            mState = STATE.PLAYING;
            updateBtn();
            
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideInfoBox();
                }
            }, 2 * 1000);
            
        } else if (mState == STATE.PLAYING) {
            pauseVideo();
            mState = STATE.PAUSE;
            updateBtn();
            
        } else if (mState == STATE.PAUSE) {
            resumeVideo();
            mState = STATE.PLAYING;
            updateBtn();
        }
        
    }
    
    private void pauseVideo() {
        if (mPlayerManager == null) return;
        
        mPlayerManager.pause();
        
    }
    
    private void resumeVideo() {
        if (mPlayerManager == null) return;
        
        mPlayerManager.resume();
    }
    
    private Runnable mHideInfo  = new Runnable() {

        @Override
        public void run() {
            hideInfoBox();
        }
    };
    
    private void showInfoBox() {
        if (inSwitching)
            return;
        
//        Log.v("order", "removeCallbacks:" + mHideInfo.toString());
        mHandler.removeCallbacks(mHideInfo);
        mHandler.postDelayed(mHideInfo, 5 * 1000);
        if (playboxshowing) {
            return;
        }
        
        inSwitching = true;
        playboxshowing = true;
        
        Animation animation = null;
        TranslateAnimation animation1 = null;
        
        animation1 = new TranslateAnimation(0, 0,
                mProcessLayout.getHeight(), 0);
        animation1.setDuration(500);
        animation1.startNow();

        animation = AnimationUtils.loadAnimation(
                FullScreenPlayActivity.this, R.anim.player_show_playbox);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mPlayBtn.requestFocus();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                inSwitching = false;
                mProgressBar.setProgress(mVol);

                updateTime();
            }
        });
        mProcessLayout.startAnimation(animation);
        mProcessLayout.setVisibility(View.VISIBLE);
    }
    
    private void hideInfoBox() {
        if (inSwitching)
            return;
        
        if (mState == STATE.FORWARDING || mState == STATE.BACKING) return;
        
        inSwitching = true;
        
        Animation animation = null;
        TranslateAnimation animation1 = null;
        
        animation1 = new TranslateAnimation(0, 0, 0,
                mProcessLayout.getHeight());
        animation1.setDuration(500);
        animation1.startNow();

        Animation out = AnimationUtils.loadAnimation(
                FullScreenPlayActivity.this, android.R.anim.fade_out);
        out.setFillAfter(true);

        animation = AnimationUtils.loadAnimation(
                FullScreenPlayActivity.this, R.anim.player_hide_playbox);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mProcessLayout.setVisibility(View.GONE);
                inSwitching = false;
                playboxshowing = false;
                
                mHandler.removeCallbacks(mGetTime);
            }
        });
        mProcessLayout.startAnimation(animation);
    }
    
    private boolean playboxshowing = false;
    private boolean inSwitching = false;// 切换模式期间不允许再次切换（快速切换来不及显示动画）
    
    private void updateTime() {
        if (playboxshowing) {
            mHandler.postDelayed(mGetTime, 1 * 1000);
        }
    }
    
    private String getTime() {
        if (mPlayerManager != null && mIsStartPlay) {
                mCurrentTime = mPlayerManager.getPlayerTime();
        }
        
        StringBuilder time = new StringBuilder(Utils.formatTime(mCurrentTime));
        time.append("/");
        time.append(Utils.formatTime(mTotal));
        
        return time.toString();
    }
    
    private void showExitDialog() {
//        new AlertDialog.Builder(this)
//                .setMessage(R.string.exit_vod)
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (null != dialog) {
//                            dialog.dismiss();
//                            dialog = null;
//                        }
//                    }
//                })
//                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // TODO
//                        finish();
//                    }
//                }).show();
//        
//        
        ExitDialog dialog = new ExitDialog(this);
        dialog.show();
    }
    
    private GetTime mGetTime = new GetTime();
    private class GetTime implements Runnable {

        @Override
        public void run() {
            mTotalTimeTv.setText(getTime());
            
            updateTime();
            
        }
        
    }
    
    private Subtitle subtitle ;
    private TextView mPopupText;
    private SubtitleReceiver mSubtitleReceiver;
    Handler myHandler = new Handler() {// 在主线程中创建Handler对象
        public void handleMessage(Message msg) {// 处理消息
            switch (msg.what) {
                case HostActivity.SHOW_SUBTEXT:
                    Log.d("shine", "handle show subtext");
                    showPoptext(subtitle);
                    break;
                case HostActivity.HIDE_SUBTEXT:
                    hidePoptest();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    
    private void showPoptext(Subtitle subtitle) {
        if (mPopupText != null && subtitle != null) {
            mPopupText.setVisibility(View.VISIBLE);
            mPopupText.setText(subtitle.getScrolltextcontent());
            if (!TextUtils.isEmpty(subtitle.getScrolltextbackgourd()))
                mPopupText.setBackgroundColor(Color.parseColor(subtitle.getScrolltextbackgourd())) ;
            mPopupText.setTextColor(Color.parseColor(subtitle.getScrolltextcolor())) ;
            mPopupText.setTextSize(Float.parseFloat(subtitle.getScrolltextsize())) ;
            
            String coordinate = subtitle.getScrolltextcoordinate() ;
            int top = Integer.parseInt(coordinate.split(",")[0]) ;
            int left = Integer.parseInt(coordinate.split(",")[1]) ;
            
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mPopupText.getLayoutParams();
            params.setMargins(left, 0, 0, top);// 通过自定义坐标来放置你的控件
            mPopupText.setLayoutParams(params);
        }
    }
    
    private void hidePoptest() {
        mPopupText.setVisibility(View.INVISIBLE);
    }
    
    public void onEvent(SubtitleEvent event) {
        this.subtitle = event.result;
        
        if (null != subtitle) {
            Log.i("shine", "subtitle onEvent " + subtitle.toString());
            
            AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

            // 开始播放滚动字幕设置
            Intent intent = new Intent();
            intent.setAction(BroadcastAction.SUBTITLE);
            intent.putExtra(HostActivity.SHOW_SUBTEXT_KEY, true);
            int requestCode = 0;
            PendingIntent pendIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            
            Calendar c = Calendar.getInstance() ;  
            // 比当前时间小（超过了当前时间立即执行）
            long triggerAtTime = c.getTimeInMillis() + 2 * 1000 ;
            
            // 比当前时间大（延迟执行）
            if (Utils.compareTo(new Date(), subtitle.getScrolltextstarttime()) < 0) {
                Date show = Utils.parser(subtitle.getScrolltextstarttime()) ;
                triggerAtTime = show.getTime() ;
            }
            
            alarmMgr.set(AlarmManager.RTC, triggerAtTime, pendIntent);
            
            // 暂停播放滚动字幕设置
            Intent intent2 = new Intent();
            intent2.setAction(BroadcastAction.SUBTITLE);
            intent2.putExtra(HostActivity.SHOW_SUBTEXT_KEY, false);
            PendingIntent pendIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 1,
                    intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            
            Date show = Utils.parser(subtitle.getScrolltextendtime()) ;
            long time = show.getTime() ;
            alarmMgr.set(AlarmManager.RTC, time, pendIntent2);
        } else {
            mPopupText.setVisibility(View.INVISIBLE) ;
        }
    }
    
    public void onEvent(GetToolDataEvent event) {
        ToolBarData toolbar = event.data;
        
        if (toolbar != null && toolbar.getMsgNum() > 0) {
            mMsgIv.setVisibility(View.VISIBLE);
        } else {
            mMsgIv.setVisibility(View.GONE);
        }

    }

}
