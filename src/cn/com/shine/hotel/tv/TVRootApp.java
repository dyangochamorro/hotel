/**
 * MStar Software
 * Copyright (c) 2011 - 2012 MStar Semiconductor, Inc. All rights reserved.
 *
 * All software, firmware and related documentation herein (閳ユ发Star Software閿� are 
 * intellectual property of MStar Semiconductor, Inc. (閳ユ发Star閿� and protected by 
 * law, including, but not limited to, copyright law and international treaties. 
 * Any use, modification, reproduction, retransmission, or republication of all 
 * or part of MStar Software is expressly prohibited, unless prior written 
 * permission has been granted by MStar. 
 *
 * By accessing, browsing and/or using MStar Software, you acknowledge that you 
 * have read, understood, and agree, to be bound by below terms (閳ユ翻erms閿� and to 
 * comply with all applicable laws and regulations:
 *
 * 1. MStar shall retain any and all right, ownership and interest to MStar 
 * Software and any modification/derivatives thereof.  No right, ownership, 
 * or interest to MStar Software and any modification/derivatives thereof is 
 * transferred to you under Terms.
 *
 * 2. You understand that MStar Software might include, incorporate or be supplied 
 * together with third party閳ユ獨 software and the use of MStar Software may require 
 * additional licenses from third parties.  Therefore, you hereby agree it is your 
 * sole responsibility to separately obtain any and all third party right and 
 * license necessary for your use of such third party閳ユ獨 software. 
 *
 * 3. MStar Software and any modification/derivatives thereof shall be deemed as 
 * MStar閳ユ獨 confidential information and you agree to keep MStar閳ユ獨 confidential 
 * information in strictest confidence and not disclose to any third party.  
 *	
 * 4. MStar Software is provided on an 閳ユ穾S IS閿�basis without warranties of any kind. 
 * Any warranties are hereby expressly disclaimed by MStar, including without 
 * limitation, any warranties of merchantability, non-infringement of intellectual 
 * property rights, fitness for a particular purpose, error free and in conformity 
 * with any international standard.  You agree to waive any claim against MStar for 
 * any loss, damage, cost or expense that you may incur related to your use of MStar 
 * Software.  In no event shall MStar be liable for any direct, indirect, incidental 
 * or consequential damages, including without limitation, lost of profit or revenues, 
 * lost or damage of data, and unauthorized system use.  You agree that this Section 4 
 * shall still apply without being affected even if MStar Software has been modified 
 * by MStar in accordance with your request or instruction for your use, except 
 * otherwise agreed by both parties in writing.
 *
 * 5. If requested, MStar may from time to time provide technical supports or 
 * services in relation with MStar Software to you for your use of MStar Software 
 * in conjunction with your or your customer閳ユ獨 product (閳ユ藩ervices閿�.  You understand 
 * and agree that, except otherwise agreed by both parties in writing, Services are 
 * provided on an 閳ユ穾S IS閿�basis and the warranty disclaimer set forth in Section 4 
 * above shall apply.  
 *
 * 6. Nothing contained herein shall be construed as by implication, estoppels or 
 * otherwise: (a) conferring any license or right to use MStar name, trademark, 
 * service mark, symbol or any other identification; (b) obligating MStar or any 
 * of its affiliates to furnish any person, including without limitation, you and 
 * your customers, any assistance of any kind whatsoever, or any information; or 
 * (c) conferring any license or right under any intellectual property right.
 *
 * 7. These terms shall be governed by and construed in accordance with the laws 
 * of Taiwan, R.O.C., excluding its conflict of law rules.  Any and all dispute 
 * arising out hereof or related hereto shall be finally settled by arbitration 
 * referred to the Chinese Arbitration Association, Taipei in accordance with 
 * the ROC Arbitration Law and the Arbitration Rules of the Association by three (3) 
 * arbitrators appointed in accordance with the said Rules.  The place of 
 * arbitration shall be in Taipei, Taiwan and the language shall be English.  
 * The arbitration award shall be final and binding to both parties.
 */

package cn.com.shine.hotel.tv;

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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.mstar.tv.service.aidl.BoolArrayList;
import com.mstar.tv.service.aidl.Constants;
import com.mstar.tv.service.skin.AudioSkin;
import com.mstar.tv.service.skin.CommonSkin;
import com.mstar.tv.service.skin.PictureSkin;
import com.mstar.tv.service.skin.PipSkin;
import com.mstar.tv.service.skin.S3DSkin;
import com.mstar.tv.service.skin.TimerSkin;
import com.shine.hotels.R;
import com.tvos.common.vo.TvOsType.EnumLanguage;

public class TVRootApp extends Application {
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

    private AudioSkin audioSkin;

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
            audioSkin.connect(null);
            pictureSkin.connect(null);
            timerSkin.connect(null);
            pipSkin.connect(null);
            commonSkin.connect(handler);
            s3dSkin.connect(null);
            Looper.loop();
        }
    };

    /**
     * end modefied by jachensy 2012-6-28
     */

    @Override
    public void onCreate() {
        super.onCreate();
        initTvDeskProvider();
        pipSkin = new PipSkin(this);
        audioSkin = new AudioSkin(this);
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
       
        /**
         * end modefied by jachensy 2012-6-28
         */
    }

    @Override
    public void onTerminate() {
        audioSkin.disconnect();
        pictureSkin.disconnect();
        timerSkin.disconnect();
        pipSkin.disconnect();
        commonSkin.disconnect();
        s3dSkin.disconnect();
        this.unregisterReceiver(rootBroadCastReceiver);
        super.onTerminate();
    }
    
    private void initConfigData() {
        Cursor snconfig = getContentResolver().query(Uri.parse("content://mstar.tv.usersetting/snconfig"), null, null,
                null, null);
        if(snconfig!=null)
        {
            if (snconfig.moveToFirst()) {
                isPVREnable = snconfig.getInt(snconfig.getColumnIndex("PVR_ENABLE")) == 1 ? true : false;
                isTTXEnable = snconfig.getInt(snconfig.getColumnIndex("TTX_ENABLE")) == 1 ? true : false;
            }        
            snconfig.close();
        }
        else
        {
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

    public AudioSkin getAudioSkin() {
        return audioSkin;
    }

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

    
    private void initDBFiles() {
        File dir = new File("/tvdatabase/Database");

        if (!dir.exists() || !dir.isDirectory())

        {

            dir.mkdir();

        }

          
        File fileFactory = new File(dir, "factory.db");

        if (!fileFactory.exists()) {

            loadDbFile(R.raw.factory, fileFactory);

        }  
        File fileUserSetting = new File(dir, "user_setting.db");

        if (!fileUserSetting.exists()) {

            loadDbFile(R.raw.user_setting, fileUserSetting);

        }
    }

    private void loadDbFile(int rawId, File file) {
        InputStream dbInputStream = getResources().openRawResource(rawId);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = dbInputStream.read(bytes)) > 0) {
                fos.write(bytes, 0, length);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                dbInputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
                DataBaseDeskImpl.setContext(TVRootApp.this);
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
            public CaDesk getCaManagerInstance()
            {
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
                : (languageindex == EnumLanguage.E_CHINESE.ordinal() ? Locale.SIMPLIFIED_CHINESE : Locale.TAIWAN);
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
