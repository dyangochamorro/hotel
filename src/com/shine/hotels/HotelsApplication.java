
package com.shine.hotels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import cn.com.shine.hotel.impl.CaDeskImpl;
import cn.com.shine.hotel.impl.ChannelDeskImpl;
import cn.com.shine.hotel.impl.CiDeskImpl;
import cn.com.shine.hotel.impl.CommonDeskImpl;
import cn.com.shine.hotel.impl.DataBaseDeskImpl;
import cn.com.shine.hotel.impl.DemoDeskImpl;
import cn.com.shine.hotel.impl.EpgDeskImpl;
import cn.com.shine.hotel.impl.PictureDeskImpl;
import cn.com.shine.hotel.impl.PvrDeskImpl;
import cn.com.shine.hotel.impl.S3DDeskImpl;
import cn.com.shine.hotel.impl.SettingDeskImpl;
import cn.com.shine.hotel.impl.SoundDeskImpl;
import cn.com.shine.hotel.service.CaDesk;
import cn.com.shine.hotel.service.ChannelDesk;
import cn.com.shine.hotel.service.CiDesk;
import cn.com.shine.hotel.service.CommonDesk;
import cn.com.shine.hotel.service.DataBaseDesk;
import cn.com.shine.hotel.service.DemoDesk;
import cn.com.shine.hotel.service.EpgDesk;
import cn.com.shine.hotel.service.PictureDesk;
import cn.com.shine.hotel.service.PvrDesk;
import cn.com.shine.hotel.service.S3DDesk;
import cn.com.shine.hotel.service.SettingDesk;
import cn.com.shine.hotel.service.SoundDesk;
import cn.com.shine.hotel.service.TvDeskProvider;
import cn.com.shine.hotel.tv.LittleDownTimer;
import cn.com.shine.hotel.tv.TimeZoneSetter;

import com.ics.mm.PlayerManager;
import com.mstar.tv.service.aidl.BoolArrayList;
import com.mstar.tv.service.aidl.Constants;
import com.mstar.tv.service.skin.AudioSkin;
import com.mstar.tv.service.skin.CommonSkin;
import com.mstar.tv.service.skin.PictureSkin;
import com.mstar.tv.service.skin.PipSkin;
import com.mstar.tv.service.skin.S3DSkin;
import com.mstar.tv.service.skin.TimerSkin;
import com.shine.hotels.center.CenterManager;
import com.shine.hotels.ui.theme.ThemeManager;
import com.tvos.common.vo.TvOsType.EnumLanguage;

public class HotelsApplication extends Application {
    public static final String ACTION_PLAY_FULL_SCREEN = "com.shine.hotels.action.PLAY_FULL_SCREEN";

    private static String sCurrentLang = "CHN";
    
    private static Context sContext;
    
    public static int sMusicVol = -1;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        
        sContext = this;

        ThemeManager theme = ThemeManager.get(getApplicationContext());
        theme.setTheme("com.shine.hoteltheme");

        CenterManager centerManager = CenterManager.get(getApplicationContext());
        centerManager.init();

        initTvDeskProvider();
        pipSkin = new PipSkin(this);
//        audioSkin = new AudioSkin(this);
        pictureSkin = new PictureSkin(this);
        timerSkin = new TimerSkin(this);
        commonSkin = new CommonSkin(this);
        s3dSkin = new S3DSkin(this);
        initConfigData();
        /**
         * start modefied by jachensy 2012-6-28
         */
        Thread t = new Thread(run);
        t.start();
        new TimeZoneSetter(this).updateTimeZone();
        tvDeskProvider.getCommonManagerInstance().disableTvosIr();

    }
    
    public static void showText() {
        Toast.makeText(sContext, R.string.network_exception, Toast.LENGTH_LONG);
    }

    public static void setCurrentLang(String lang) {
        sCurrentLang = lang;
    }

    public static String getCurrentLang() {
        return sCurrentLang;
    }

    // for hot key
    public static boolean isSoundSettingDirty = false;

    public static boolean isVideoSettingDirty = false;

    public static boolean isPicModeSettingDirty = false;

    public static boolean isFactoryDirty = false;

    // for misc
    public static boolean isMiscSoundDirty = false;

    public static boolean isMiscPictureDirty = false;

    public static boolean isMiscS3DDirty = false;

    public static boolean isSourceDirty = false;

    public static boolean isLoadDBOver = false;

    private static EnumTextStatus textStatus = EnumTextStatus.TEXT_STATUS_NONE;

    private boolean isPVREnable = false;

    private boolean isTTXEnable = false;

    public enum EnumTextStatus {
        TEXT_STATUS_TTX, TEXT_STATUS_MHEG5, TEXT_STATUS_HBBTV, // #if
                                                               // defined(HBBTV_ENABLE)
        TEXT_STATUS_GINGA, TEXT_STATUS_NONE,
    }

    // private TvDeskProvider tvDeskProviderOld;//this one connects service

    private TvDeskProvider tvDeskProvider;// this one call method of class
                                          // directly

    private PipSkin pipSkin;

//    private AudioSkin audioSkin;

    private PictureSkin pictureSkin;

    private CommonSkin commonSkin;

    private TimerSkin timerSkin;

    private S3DSkin s3dSkin;

    private RootBroadCastReceiver rootBroadCastReceiver;

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == Constants.CONNECTION_OK) {
                // commonSkin.EnableAutoSourceSwitch();
            }
        };
    };

    /**
     * start modefied by jachensy 2012-6-28
     */
    Runnable run = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tvDeskProvider.getDataBaseManagerInstance().openDB();
            loadEssentialDataFromDB();
            isLoadDBOver = true;
            tvDeskProvider.getDataBaseManagerInstance().closeDB();
            rootBroadCastReceiver = new RootBroadCastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH");
            registerReceiver(rootBroadCastReceiver, filter);
            initLittleDownCounter();
//            audioSkin.connect(null);
            pictureSkin.connect(null);
            timerSkin.connect(null);
            pipSkin.connect(null);
            commonSkin.connect(handler);
            s3dSkin.connect(null);
            Looper.loop();
        }
    };

    @Override
    public void onTerminate() {
        sContext = null;
//        audioSkin.disconnect();
        pictureSkin.disconnect();
        timerSkin.disconnect();
        pipSkin.disconnect();
        commonSkin.disconnect();
        s3dSkin.disconnect();
        this.unregisterReceiver(rootBroadCastReceiver);
        super.onTerminate();
    }

    private void initConfigData() {
        Cursor snconfig = getContentResolver().query(
                Uri.parse("content://mstar.tv.usersetting/snconfig"), null, null, null, null);
        if (snconfig != null) {
            if (snconfig.moveToFirst()) {
                isPVREnable = snconfig.getInt(snconfig.getColumnIndex("PVR_ENABLE")) == 1 ? true
                        : false;
                isTTXEnable = snconfig.getInt(snconfig.getColumnIndex("TTX_ENABLE")) == 1 ? true
                        : false;
            }
            snconfig.close();
        } else {
            isPVREnable = true;
            isTTXEnable = true;
        }
    }

    public boolean isPVREnable() {
        return isPVREnable;
    }

    public boolean isTTXEnable() {
        return isTTXEnable;
    }

    public TvDeskProvider getTvDeskProvider() {
        return tvDeskProvider;
    }

//    public AudioSkin getAudioSkin() {
//        return audioSkin;
//    }

    public PictureSkin getPictureSkin() {
        return pictureSkin;
    }

    public TimerSkin getTimerSkin() {
        return timerSkin;
    }

    public PipSkin getPipSkin() {
        return pipSkin;
    }

    public CommonSkin getCommonSkin() {
        return commonSkin;
    }

    public S3DSkin getS3DSkin() {
        return s3dSkin;
    }

    private void initTvDeskProvider() {
        tvDeskProvider = new TvDeskProvider() {
            CommonDesk comManager = null;

            PictureDesk pictureManager = null;

            DataBaseDeskImpl dataBaseManager = null;

            SettingDesk settingManager = null;

            ChannelDesk channelManager = null;

            SoundDesk soundManager = null;

            S3DDesk s3dManager = null;

            DemoDesk demoManager = null;

            EpgDesk epgManager = null;

            PvrDesk pvrManager = null;

            CiDesk ciManager = null;

            CaDesk caManager = null;

            @Override
            public void initTvSrvProvider() {
            }

            @Override
            public CommonDesk getCommonManagerInstance() {
                comManager = CommonDeskImpl.getInstance();
                return comManager;
            }

            @Override
            public PictureDesk getPictureManagerInstance() {
                pictureManager = PictureDeskImpl.getPictureMgrInstance();
                return pictureManager;
            }

            @Override
            public DataBaseDesk getDataBaseManagerInstance() {
                DataBaseDeskImpl.setContext(HotelsApplication.this);
                dataBaseManager = DataBaseDeskImpl.getDataBaseMgrInstance();
                return dataBaseManager;
            }

            @Override
            public SettingDesk getSettingManagerInstance() {
                settingManager = SettingDeskImpl.getSettingMgrInstance();
                return settingManager;
            }

            @Override
            public ChannelDesk getChannelManagerInstance() {
                channelManager = ChannelDeskImpl.getChannelMgrInstance();
                return channelManager;
            }

            @Override
            public SoundDesk getSoundManagerInstance() {
                soundManager = SoundDeskImpl.getSoundMgrInstance();
                return soundManager;
            }

            @Override
            public S3DDesk getS3DManagerInstance() {
                s3dManager = S3DDeskImpl.getS3DMgrInstance();
                return s3dManager;
            }

            @Override
            public DemoDesk getDemoManagerInstance() {
                demoManager = DemoDeskImpl.getDemoMgrInstance();
                return demoManager;
            }

            @Override
            public EpgDesk getEpgManagerInstance() {
                epgManager = EpgDeskImpl.getEpgMgrInstance();
                return epgManager;
            }

            @Override
            public PvrDesk getPvrManagerInstance() {
                pvrManager = PvrDeskImpl.getPvrMgrInstance();
                return pvrManager;
            }

            @Override
            public CiDesk getCiManagerInstance() {
                ciManager = CiDeskImpl.getCiMgrInstance();
                return ciManager;
            }

            @Override
            public CaDesk getCaManagerInstance() {
                caManager = CaDeskImpl.getCaMgrInstance();
                return caManager;
            }
        };

    }

    private void loadEssentialDataFromDB() {
        tvDeskProvider.getDataBaseManagerInstance().loadEssentialDataFromDB();
    }

    public void initLanguageCfig() {
        int languageindex = tvDeskProvider.getSettingManagerInstance().GetOsdLanguage().ordinal();
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        config.locale = (languageindex == EnumLanguage.E_ENGLISH.ordinal()) ? Locale.ENGLISH
                : (languageindex == EnumLanguage.E_CHINESE.ordinal() ? Locale.SIMPLIFIED_CHINESE
                        : Locale.TAIWAN);
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(config, dm);
        Log.d("language", "" + config.locale.toString());
    }

    private void initLittleDownCounter() {
        LittleDownTimer.getInstance().start();
        int value = tvDeskProvider.getSettingManagerInstance().getOsdTimeoutSecond();
        if (value < 1) {
            Log.d("time out data problem", "the unit is ms in db");
            value = 5;
        }
        if (value > 30) {
            LittleDownTimer.stopMenu();
        } else {
            LittleDownTimer.setMenuStatus(value);
        }
    }

    // Ui setting root broadcast receiver.
    private class RootBroadCastReceiver extends BroadcastReceiver {
        private BoolArrayList boolArrayList;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // mute
            if (action.equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH")) {
                // boolArrayList = commonSkin.GetInputSourceStatus();
                // int index = intent.getIntExtra("SwitchSourceIndex", -1);
                // Log.d("root broadcast reciever!","COMMON_EVENT_SIGNAL_AUTO_SWITCH"
                // + boolArrayList + "  index" + index);
                // if (index != -1)
                // {
                // commonSkin.ForbidDetection();
                // Log.d("root broadcast reciever!",EN_INPUT_SOURCE_TYPE.values()[index]
                // + "");
                // commonSkin.SetInputSource(EN_INPUT_SOURCE_TYPE.values()[index]);
                // commonSkin.AllowDetection();
                // }
            } else {
                Log.e("root broadcast reciever!", "Unknown Key Event!");
            }
        }
    }

    /**
     * set tv text mode, notes that your system need support the capability
     * 
     * @param enTextStatus
     */
    public void setTvTextMode(EnumTextStatus enTextStatus) {
        this.textStatus = enTextStatus;
    }

    public EnumTextStatus getTvTextMode() {
        return this.textStatus;
    }

}
