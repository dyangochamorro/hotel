
package com.shine.hotels.ui;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.shine.hotel.util.Constant;

import com.mstar.tv.service.aidl.EN_INPUT_SOURCE_TYPE;
import com.mstar.tv.service.interfaces.ITvServiceServer;
import com.mstar.tv.service.interfaces.ITvServiceServerCommon;
import com.shine.hotels.HotelsApplication;
import com.shine.hotels.R;
import com.shine.hotels.controller.ControllerManager;
import com.shine.hotels.controller.Events.BootInfoEvent;
import com.shine.hotels.controller.Events.WelcomeEvent;
import com.shine.hotels.controller.Request;
import com.shine.hotels.controller.Request.Builder;
import com.shine.hotels.controller.WelcomeController;
import com.shine.hotels.io.APIManager;
import com.shine.hotels.io.model.BootInfo;
import com.shine.hotels.io.model.Welcome;
import com.shine.hotels.io.model.WelcomeList;
import com.shine.hotels.service.MusicService;
import com.shine.hotels.ui.hotelintroduction.PPTActivity;
import com.shine.systemmanage.aidl.IShineSystemManage;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

public class WelcomeActivity extends Activity implements OnClickListener, OnFocusChangeListener {
    // 10 未授权 11 已授权 12 授权已到期
    private static final int NOT_AUTH = 10;

    private static final int HAD_AUTH = 11;

    private static final int EXPIRED_AUTH = 12;

    private static final int[] PWD = new int[] {
            KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_DPAD_RIGHT,
            KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN
    };

    private int[] mPwd;

    private WelcomeController mController;

    private View mLeftView;
    private ImageView mLeftLogo;
    private View mRighView;

    private TextView contentmrView;

    private TextView contentView;

    private String chnContentmrValue;

    private String chnContentValue;

    private String engContentmrValue;

    private String engContentValue;

    private ImageButton chnImageView;

    private TextView chnTextView;

    private ImageButton engImageView;

    private TextView engTextView;

    private TextView mAuthTv;

    private String chnValue;

    private String engValue;

    private String chnImageUrlNo;

    private String chnImageUrlYes;

    private String engImageUrlNo;

    private String engImageUrlYes;

    private boolean mIsFirst = false;

    private final static String REQUEST = "REQUESTED";

    Animation leftAnim;

    Animation rightAnim;

//    private MusicService mService;

    boolean mBound = false;

    private boolean mForbidKeyEvent = true;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get
            // LocalService instance
//            MusicBinder binder = (MusicBinder)service;
//            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        
//        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
//        ViewGroup root = (ViewGroup)decor.getChildAt(0);
//        root.setVisibility(View.INVISIBLE);

        bindService(new Intent("com.shine.systemmanage.aidl"), serviceConnection, Context.BIND_AUTO_CREATE);

         Log.w("shine", "onCreate...");
        mAuthTv = (TextView)findViewById(R.id.auth_tv);

        contentmrView = (TextView)findViewById(R.id.content_mr);
        contentView = (TextView)findViewById(R.id.content);

        chnImageView = (ImageButton)findViewById(R.id.chn_image);
        chnImageView.requestFocus();
        chnImageView.setOnClickListener(this);
        chnImageView.setOnFocusChangeListener(this);

        chnTextView = (TextView)findViewById(R.id.chn_text);

        engImageView = (ImageButton)findViewById(R.id.eng_image);
        engImageView.setOnClickListener(this);
        engImageView.setOnFocusChangeListener(this);

        engTextView = (TextView)findViewById(R.id.eng_text);

        mLeftView = findViewById(R.id.left_view_layout);
        mRighView = findViewById(R.id.right_view);
        mLeftLogo = (ImageView)findViewById(R.id.left_view);
        OpenningAnimationListener listener = new OpenningAnimationListener();
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.slide_left_exit);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.slide_right_exit);
        leftAnim.setAnimationListener(listener);

        if (savedInstanceState == null) {
            new InitHostTask().execute();
        } else {
            doRequest();
        }

        ITvServiceServer tvService = ITvServiceServer.Stub.asInterface(ServiceManager
                .checkService(Context.TV_SERVICE));
        if (tvService != null) {
            try {
                ITvServiceServerCommon commonService = tvService.getCommonManager();
                commonService.SetInputSource(EN_INPUT_SOURCE_TYPE.E_INPUT_SOURCE_DTV);
                System.out.println("======ITvServiceServerCommon====");
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onDestroy();
        Log.e("shine", "onDestroy killProcess");
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    private IShineSystemManage myService = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            myService = IShineSystemManage.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            
        }
        
    };
    
    public void onEvent(BootInfoEvent event) {

        BootInfo info = event.bootInfo;
        if (info == null) {
            mAuthTv.setVisibility(View.VISIBLE);
            mAuthTv.setText(R.string.network_exception);
            mForbidKeyEvent = false;
            return;
        }

        final String time = info.getTime();
        if (!TextUtils.isEmpty(time) && myService != null) {
            try {
                Date server = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
//                Log.d("shine", "s=" + server.getTime() + " c=" + System.currentTimeMillis());
                if (Math.abs(server.getTime() - System.currentTimeMillis()) > (5 * 60 * 1000)) {
                    myService.SetRtcTime(time);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        // logo
        Picasso.with(this).load(info.getLogo()).into(mLeftLogo);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup decor = (ViewGroup) getWindow().getDecorView();
//                ViewGroup root = (ViewGroup)decor.getChildAt(0);
//                root.setVisibility(View.VISIBLE);
//            }
//        }, 500);

        final int num = info.getNum();
        // Log.d("shine", "onEvent BootInfo...num=" + num);
        switch (num) {
            case NOT_AUTH:
                mAuthTv.setVisibility(View.VISIBLE);
                mAuthTv.setText(R.string.not_auth);
                mForbidKeyEvent = false;
                return;
            case EXPIRED_AUTH:
                mAuthTv.setVisibility(View.VISIBLE);
                mAuthTv.setText(R.string.expired_auth);
                mForbidKeyEvent = false;
                return;
            default:
                mAuthTv.setVisibility(View.GONE);
                break;
        }

        switch (info.getType()) {
            case 1:
                playPics(info.getUrls());
                break;
            case 2:
                playVideo(info.getUrls());
                break;

            default:
                openTheDoor();
                break;
        }
    }

    public void onEvent(WelcomeEvent<WelcomeList> event) {
        // Log.d("shine", "onEvent WelcomeEvent...");
        WelcomeList result = event.result;

        List<Welcome> list = result.getmWelcomes();

        try {
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i > 2)
                        break;
                    Welcome welcome = list.get(i);

                    switch (i) {
                        case 0:
                            chnTextView.setText(welcome.getLanguageName());
                            chnValue = welcome.getLanguagevalue();

                            chnContentmrValue = welcome.getCustomerName();
                            chnContentValue = welcome.getWelcoming();

                            if (1 == welcome.getStatus()) {
                                contentmrView.setText(welcome.getCustomerName());
                                contentView.setText(welcome.getWelcoming());
                                // Picasso.with(this).load(welcome.getLanguagePicYes()).placeholder(R.drawable.cn_btn_down).into(chnImageView);
                                chnImageView.requestFocus();
                                chnImageView.setBackgroundResource(R.drawable.cn_btn_down);
                                engImageView.setBackgroundResource(R.drawable.en_btn_up);
                            } else {
                                // Picasso.with(this).load(welcome.getLanguagePicNo()).placeholder(R.drawable.cn_btn_down).into(chnImageView);
                            }

                            chnImageUrlNo = welcome.getLanguagePicNo();
                            chnImageUrlYes = welcome.getLanguagePicYes();
                            break;
                        case 1:
                            engTextView.setText(welcome.getLanguageName());
                            engValue = welcome.getLanguagevalue();

                            engContentmrValue = welcome.getCustomerName();
                            engContentValue = welcome.getWelcoming();

                            if (1 == welcome.getStatus()) {
                                contentmrView.setText(welcome.getCustomerName());
                                contentView.setText(welcome.getWelcoming());
                                // Picasso.with(this).load(welcome.getLanguagePicYes()).placeholder(R.drawable.cn_btn_down).into(engImageView);
                                engImageView.requestFocus();
                                chnImageView.setBackgroundResource(R.drawable.cn_btn_up);
                                engImageView.setBackgroundResource(R.drawable.en_btn_down);
                            } else {
                                // Picasso.with(this).load(welcome.getLanguagePicNo()).placeholder(R.drawable.cn_btn_down).into(engImageView);
                            }

                            engImageUrlNo = welcome.getLanguagePicNo();
                            engImageUrlYes = welcome.getLanguagePicYes();
                            break;
                        case 2:
                            break;
                        default:
                            break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final int action = event.getAction();
        final int keyCode = event.getKeyCode();
        
        if (action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }

        if (action == KeyEvent.ACTION_DOWN && !mForbidKeyEvent) {
            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    mPwd = new int[PWD.length];
                    mPwd[0] = KeyEvent.KEYCODE_DPAD_LEFT;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_DPAD_DOWN:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (mPwd != null) {
                        for (int i = 0; i < PWD.length; i++) {
                            if (mPwd[i] == 0) {
                                mPwd[i] = keyCode;
                                break;
                            }
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    if (mPwd != null && mPwd.length == PWD.length) {
                        int i = 0;
                        // StringBuffer sb = new StringBuffer();
                        for (; i < PWD.length; i++) {
                            // sb.append(mPwd[i]);
                            // sb.append(" ");

                            if (mPwd[i] != PWD[i])
                                break;
                        }

                        // Log.w("shine", "pwd=" + sb.toString() + " length=" +
                        // PWD.length);

                        if (i == PWD.length) {
                            // finish();
                            if (mBound) {
                                unbindService(mConnection);
                                mBound = false;
                            }
                            Log.e("shine", "onDestroy killProcess");
                            android.os.Process.killProcess(android.os.Process.myPid());
                            return true;
                        }
                    }
                    break;

                default:
                    break;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Log.d("shine", "onSaveInstanceState...");
        outState.putBoolean(REQUEST, true);
    }

    @Override
    protected void onResume() {
         Log.d("shine", "onResume...");
        super.onResume();

        if (mIsFirst) {
            openTheDoor();
            mIsFirst = false;
        }

    }

    @Override
    public void onStart() {
         Log.d("shine", "onStart...");
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if (!mIsOpen)
            return;

        int id = v.getId();

        Resources resources = getResources();// 获得res资源对象

        Configuration config = resources.getConfiguration();// 获得设置对象

        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。

        if (id == R.id.chn || id == R.id.chn_image) {
            HotelsApplication.setCurrentLang(chnValue);

            config.locale = Locale.SIMPLIFIED_CHINESE; // 简体中文

            resources.updateConfiguration(config, dm);

        } else if (id == R.id.eng || id == R.id.eng_image) {
            HotelsApplication.setCurrentLang(engValue);

            config.locale = Locale.US; // 英文

            resources.updateConfiguration(config, dm);
        }

        Intent intent = new Intent(this, HostActivity.class);
        startActivity(intent);

    }

    @Override
    public void onFocusChange(View v, boolean flag) {
        int id = v.getId();
        if (flag) {
            if (id == R.id.chn || id == R.id.chn_image) {
                contentmrView.setText(chnContentmrValue);
                contentView.setText(chnContentValue);
                chnImageView.setBackgroundResource(R.drawable.cn_btn_down);
                engImageView.setBackgroundResource(R.drawable.en_btn_up);
            } else if (id == R.id.eng || id == R.id.eng_image) {
                contentmrView.setText(engContentmrValue);
                contentView.setText(engContentValue);
                chnImageView.setBackgroundResource(R.drawable.cn_btn_up);
                engImageView.setBackgroundResource(R.drawable.en_btn_down);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.d("shine", "onActivityResult...");
        if (requestCode == 1) {
            mIsFirst = true;
        }
    }

    private void playPics(List<String> urls) {
        if (urls != null && urls.size() > 0) {
            Intent intent = new Intent(this, PPTActivity.class);
            intent.putStringArrayListExtra(PPTActivity.INTENT_KEY_PICS, (ArrayList<String>)urls);
            startActivityForResult(intent, 1);
        } else {
            openTheDoor();
        }
    }

    private void playVideo(List<String> urls) {
        if (urls != null && urls.size() > 0) {
            String url = urls.get(0);

            Intent intent = new Intent(HotelsApplication.ACTION_PLAY_FULL_SCREEN);
            intent.putExtra(FullScreenPlayActivity.INTENT_KEY_MOVIE_URL, url);
            intent.putExtra(FullScreenPlayActivity.INTENT_KEY_PLAY_TYPE,
                    FullScreenPlayActivity.TYPE_BOOT);
            startActivityForResult(intent, 1);
        } else {
            openTheDoor();
        }
    }

    private boolean mIsOpen;

    private void openTheDoor() {
        // Log.d("shine", "openTheDoor...");
        mLeftView.startAnimation(leftAnim);
        mRighView.startAnimation(rightAnim);
        mIsOpen = true;
        mForbidKeyEvent = false;
        
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private class OpenningAnimationListener implements AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mLeftView.setVisibility(View.GONE);
            mRighView.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    private class HostHandler extends DefaultHandler {
        String host = null;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            if ("commuip".equals(localName)) {
                host = localName;
            }
        }

        public String getHost() {
            return host;
        }
    }
    
    private void doRequest() {
        mController = (WelcomeController)ControllerManager.newController(WelcomeActivity.this,
                WelcomeController.class);
        Builder builder = new Builder();
        Request request = builder.obtain(Request.Action.WELCOME_INIT).getResult();
        mController.handle(request);

        Request request2 = builder.obtain(Request.Action.BOOT_INFO).getResult();
        mController.handle(request2);
    }

    private class InitHostTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String host = "";
            try {
                File file = new File(Constant.HOST_PATH + Constant.HOST_XML);
                if (file.exists()) {
                    host = getHost(Constant.HOST_XML);
                }
            } catch (Exception e) {

            }
            return host;
        }

        @Override
        protected void onPostExecute(String result) {
            if (TextUtils.isEmpty(result)) {
                APIManager.HOST = APIManager.DEFAULT_HOST;
            } else {
                APIManager.HOST = "http://" + result + "/hotel/";
            }

            doRequest();
//            mController = (WelcomeController)ControllerManager.newController(WelcomeActivity.this,
//                    WelcomeController.class);
//            Builder builder = new Builder();
//            Request request = builder.obtain(Request.Action.WELCOME_INIT).getResult();
//            mController.handle(request);
//
//            Request request2 = builder.obtain(Request.Action.BOOT_INFO).getResult();
//            mController.handle(request2);
        }

        public String getHost(String path) throws Exception {
            File file = new File(Constant.HOST_PATH + path);
            if (file.exists()) {
                char[] buffer = new char[32];

                FileReader fr = new FileReader(file);
                fr.read(buffer);

                String host = new String(buffer);

                return host.trim();
            }

            return null;
        }
    }

}
