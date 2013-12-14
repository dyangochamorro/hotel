package com.shine.hotels.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.com.shine.hotel.tv.FullTVActivity;

import com.shine.hotels.R;
import com.shine.hotels.center.BroadcastAction;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.GetTvEvent;
import com.shine.hotels.controller.Events.SubtitleEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.SubtitleController;
import com.shine.hotels.io.model.Appreciatetv;
import com.shine.hotels.io.model.AppreciatetvList;
import com.shine.hotels.io.model.CategoryData;
import com.shine.hotels.io.model.Subtitle;
import com.shine.hotels.service.MusicService;
import com.shine.hotels.service.MusicService.MusicBinder;
import com.shine.hotels.ui.appreciatemovie.FragmentAppreciatemovie;
import com.shine.hotels.ui.appreciatemovie.FragmentAppreciatemovieShow;
import com.shine.hotels.ui.appreciatemovie.FragmentAppreciatemovieShow2;
import com.shine.hotels.ui.home.FragmentHome;
import com.shine.hotels.ui.theme.CustomerTheme;
import com.shine.hotels.ui.theme.ThemeManager;
import com.shine.hotels.ui.tools.FragmentTools;
import com.shine.hotels.util.Utils;
import com.shine.hotels.util.ViewServer;

import de.greenrobot.event.EventBus;

/**
 * 
 * 容纳各种fragment的activity
 */
public class HostActivity extends FragmentActivity {
    public static final int SHOW_SUBTEXT = 1;
    public static final int HIDE_SUBTEXT = 0;
    public static final String SHOW_SUBTEXT_KEY = "show_text"; 
    
    private FragmentManager mFragmentManager;
    private BaseFragment mFontFragment;
    
    private SubtitleController mController ;
    
    private Subtitle subtitle ;
    
    private TextView mPopupText;
//    private LinearLayout popLinearLayout ;
    
    private SubtitleReceiver mSubtitleReceiver;
    
    Handler myHandler = new Handler() {// 在主线程中创建Handler对象
        public void handleMessage(Message msg) {// 处理消息
            switch (msg.what) {
                case SHOW_SUBTEXT:
                    showPoptext(subtitle);
                    break;
                case HIDE_SUBTEXT:
                    hidePoptest();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    
    private MusicService mService;
    boolean mBound = false;
    
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicBinder binder = (MusicBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.notifyMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(new OnBackStackChangedListener() {
            
            @Override
            public void onBackStackChanged() {
                int count = mFragmentManager.getBackStackEntryCount();
                Log.i("shine", "onBackStackChanged count=" + count);
                if (count > 0) {
                    BackStackEntry entry = mFragmentManager.getBackStackEntryAt(count - 1);
                    String tag = entry.getName();
                    Log.d("shine", "getentry name=" + tag);
                    
                    mFontFragment = (BaseFragment)mFragmentManager.findFragmentByTag(tag);
                    if (mFontFragment != null) mFontFragment.onBackToFont();
                }
            }
        });

        CustomerTheme theme = ThemeManager.get(getApplicationContext())
                .getTheme();
        if (theme != null) {
            View layout = theme.getLayoutView("activity_host_layout");
            setContentView(layout);
        } else {
            setContentView(R.layout.activity_host_layout);
        }

        ViewServer.get(getApplicationContext()).addWindow(this);
        
        View root = getWindow().getDecorView().getRootView();
        mPopupText = (TextView)root.findViewById(R.id.pop_up_txt);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        if (theme != null) {
            int left = theme.getIdentifier("id", "bottom_left_layout");
            if (left != 0) {
                Fragment tools = new FragmentTools();
                fragmentTransaction.add(left, tools, UIConfig.FRAGMENT_TAG_TOOLS);
            }
            Fragment home = new FragmentHome();
            fragmentTransaction.add(theme.getIdentifier("id", "bottom_main_layout"), home,
                    UIConfig.FRAGMENT_TAG_HOME);
            
            
        } else {
            Fragment tools = new FragmentTools();
            fragmentTransaction.add(R.id.bottom_left_layout, tools,
                    UIConfig.FRAGMENT_TAG_TOOLS);
            Fragment home = new FragmentHome();
            fragmentTransaction.add(R.id.bottom_main_layout, home,
                    UIConfig.FRAGMENT_TAG_HOME);
            fragmentTransaction.addToBackStack(UIConfig.FRAGMENT_TAG_HOME);
        }
        fragmentTransaction.commit();

        mSubtitleReceiver = new SubtitleReceiver(myHandler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastAction.SUBTITLE);
        registerReceiver(mSubtitleReceiver, filter);
        
        EventBus.getDefault().register(this);
        
        mController = (SubtitleController)ControllerManager.newController(getApplicationContext(), SubtitleController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.GET_SUBTITLE).getResult();
        mController.handle(request);
        
        Request request2 = builder.obtain(Request.Action.GET_APPRECIATE_TV).getResult();
        mController.handle(request2);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        if (mBound && mService != null) {
            mService.notifyMusic();
        } else {
            Intent intent = new Intent(this, MusicService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ViewServer.get(getApplicationContext()).setFocusedWindow(this);
    }

    @Override
    protected void onDestroy() {
        ViewServer.get(getApplicationContext()).removeWindow(this);
        
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        
        EventBus.getDefault().unregister(this);
        
        unregisterReceiver(mSubtitleReceiver);
        
        super.onDestroy();
    }
    
    public MusicService getMusicService() {
        return mService;
    }
    
    private List<String> mFrequencys = new ArrayList<String>();
    public void onEvent(GetTvEvent<AppreciatetvList> event) {
        AppreciatetvList aList = event.result;
        
        // 处理List返回内容
        if (aList == null) return;
        
        // init frequency
        List<Appreciatetv> list = aList.getLists();
        List<CategoryData> datas = list.get(list.size() - 1).getData();
        mFrequencys.clear();
        for (CategoryData data : datas) {
            mFrequencys.add(data.getCategoryurl());
        }
        
    }
    
    public void onEvent(SubtitleEvent event) {
        this.subtitle = event.result;
        
        if (null != subtitle) {
//            Log.i("shine", "subtitle onEvent " + subtitle.toString());
            
        	AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

            // 开始播放滚动字幕设置
            Intent intent = new Intent();
            intent.setAction(BroadcastAction.SUBTITLE);
            intent.putExtra(SHOW_SUBTEXT_KEY, true);
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
            intent2.putExtra(SHOW_SUBTEXT_KEY, false);
            PendingIntent pendIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 1,
                    intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            
    		Date show = Utils.parser(subtitle.getScrolltextendtime()) ;
    		long time = show.getTime() ;
            alarmMgr.set(AlarmManager.RTC, time, pendIntent2);
        } else {
        	mPopupText.setVisibility(View.INVISIBLE) ;
        }
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.d("shine", "keyCode:" + event.getKeyCode() + "|action:" + event.getAction());
        
        final int keyCode = event.getKeyCode();
        final int action = event.getAction();
        
        boolean isHit = false;
        if (action == KeyEvent.ACTION_UP && null != mFontFragment) {
            switch (keyCode) {
                case 131:
                    // 电影
                    if (!(mFontFragment instanceof FragmentAppreciatemovie || 
                            mFontFragment instanceof FragmentAppreciatemovieShow ||
                            mFontFragment instanceof FragmentAppreciatemovieShow2 ||
                            mFontFragment instanceof VODPayFragment)) {
                        FragmentTransaction transaction = getSupportFragmentManager()
                                .beginTransaction();
                        Fragment movie = new FragmentAppreciatemovie();
                        transaction.add(R.id.bottom_main_layout, movie,
                                UIConfig.FRAGMENT_TAG_MOVIE_INDEX);
                        transaction.hide(mFontFragment);
                        transaction.addToBackStack(UIConfig.FRAGMENT_TAG_MOVIE_INDEX);
                        transaction.commit();
                    }
                    break;
                case 170:
                    // TV
                    Intent intent = new Intent(this, FullTVActivity.class);
                    
                    intent.putStringArrayListExtra(FullTVActivity.INTENT_KEY_DATA, (ArrayList<String>)mFrequencys);
                    startActivity(intent);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    isHit = mFontFragment.onKeyUp();
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    isHit = mFontFragment.onKeyDown();
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    isHit = mFontFragment.onKeyCenter();
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    isHit = mFontFragment.onKeyLeft();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    isHit = mFontFragment.onKeyRight();
                    break;
                case KeyEvent.KEYCODE_BACK:
                    isHit = mFontFragment.onKeyBack();
                    break;
                default:
                    break;
            }
        } 
        
        if (isHit) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // Log.v("order", "keycode=" + keyCode);
        if (mFontFragment != null)
            switch (keyCode) {
                case 36:
                    mFontFragment.onKeyVolumeDown();
                    return true;

                case 35:
                    mFontFragment.onKeyVolumeUp();
                    return true;

                case 37:
                    mFontFragment.onKeyVolumeMute();
                    return true;
            }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int arg0, KeyEvent arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onKeyMultiple(int arg0, int arg1, KeyEvent arg2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        //showPopup(this.findViewById(event.getDeviceId()));
        return super.onKeyUp(keyCode, event);
    } 
    
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
            
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mPopupText.getLayoutParams();
            params.setMargins(left, 0, 0, top);// 通过自定义坐标来放置你的控件
            mPopupText.setLayoutParams(params);
        }
    }
    
    private void hidePoptest() {
        mPopupText.setVisibility(View.INVISIBLE);
    }
    
}
