package com.ics.mm;


import java.lang.ref.WeakReference;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import android.view.SurfaceHolder;

public class PlayerManager {
    
    protected enum EVENT{
        EVENT_FROM_NATIVE,
    }

    public enum EnumMmInterfaceOptionType
    {   
        E_MM_INTERFACE_OPTION_NULL,
       E_MM_INTERFACE_OPTION_SET_STARTTIME ,
              
       E_MM_INTERFACE_OPTION_SET_TOTAL_TIME,
       
       E_MM_INTERFACE_OPTION_RESET_BUF,
       
       E_MM_INTERFACE_OPTION_ENABLE_SEAMLESS,
       
       E_MM_INTERFACE_OPTION_CHANGE_PROGRAM,

    }
    
    public enum EnumMmInterfaceNotifyType
    {
        E_MM_INTERFACE_EXIT_OK, //playback ok, and exit to ap
        E_MM_INTERFACE_EXIT_ERROR, //playback  error, and exit to ap
        E_MM_INTERFACE_SUFFICIENT_DATA, //when playing, data enough to continue play, and codec wil resume
        E_MM_INTERFACE_INSUFFICIENT_DATA, //when playing, run short of data, and codec wil pause
        E_MM_INTERFACE_START_PLAY, //player init ok, and start playing
        E_MM_INTERFACE_NULL, //playback notify null
    } 
    
    private int mNativeContext; // accessed by native methods

    private int mPlayerManagerContext; // accessed by native methods
    
    private OnPlayerEventListener mOnEventListener;

    private EventHandler mEventHandler;
    
    private int mSurfaceTexture;
    
    private SurfaceHolder mSurfaceHolder; // for surface overlay
    
    private Surface mSurface; // for surface overlay

    private void updateSurfaceScreenOn()
    {
        if (mSurfaceHolder != null)
        {
            mSurfaceHolder.setKeepScreenOn(true);
        }
    }


    public void setDisplay(SurfaceHolder sh)
    {
        mSurfaceHolder = sh;
        if (sh != null)
        {
            mSurface = sh.getSurface();
        }
        else
        {
            mSurface = null;
        }
        init(mSurface);
        updateSurfaceScreenOn();
    }
    
    

    
    private static native final void native_init();

    private native final void native_setup(Object msrv_this);

    private native final void native_finalize();
    
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        native_finalize();
    }
    
    public PlayerManager(){
        Looper looper;
        if ((looper = Looper.myLooper()) != null)
        {
            mEventHandler = new EventHandler(this, looper);
        }
        else if ((looper = Looper.getMainLooper()) != null)
        {
            mEventHandler = new EventHandler(this, looper);
        }
        else
        {
            mEventHandler = null;
        }

        native_setup(new WeakReference<PlayerManager>(this));
    }
    
    static
    {
        try
        {
            System.loadLibrary("mm_player_jni");
            native_init();
        }
        catch (UnsatisfiedLinkError e)
        {
            System.err.println("Cannot load mm_player library:\n "
                + e.toString());
        }
    }
    

    /**
     *
     * <p>
     * Title: OnPlayerEventListener
     * </p>
     *
     * <p>
     * Description: This interface define the events which relative to
     * audio operations. You can implements your own OnPlayerEventListener,
     * then register listener by
     * setOnPlayerEventListener(OnPlayerEventListener listener). Your
     * listener will be triggered when the events posted from native code.
     * The following is the sample code for creating your customized
     * Event Listener based on OnPlayerEventListener.<br>
     *
     * ---------------------------------------------------------------------
     * ---------------------------------------------<br>
     * [Sample 1] sample code to implement PlayerManager.OnPlayerEventListener
     * and register listener class to event handler.<br>
     * ---------------------------------------------------------------------
     * ---------------------------------------------<br>
     * public class MyActivity implements PlayerManager.OnPlayerEventListener<br>
     * {<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;private PlayerManager playerManager = new PlayerManager();<br>
     * <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;protected void onCreate(Bundle savedInstanceState)<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;{<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;super.onCreate(savedInstanceState);<br>
     * ...<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;playerManager.setOnPlayerEventListener(this);<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;} <br>
     * <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;public boolean onPlayEvent(PlayerManager mgr, int what);<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;{<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bundle b = new Bundle();<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b.setInt("what",what);<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// todo: transfer your Bundle class to the receiver.<br>
     *
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return true;<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;}<br>
     * ...<br>
     * }<br>
     * </p>
     * <p>
     * Copyright: Copyright (c) 2011
     * </p>
     *
     * <p>
     * Company: Mstarsemi Inc.
     * </p>
     *
     * @version 1.0
     */
    public interface OnPlayerEventListener
    {
        /**
         * Called to indicate an info or a warning.
         *
         * @param mgr the AudioManager the info pertains to.
         * @param what the type of info or warning.
         * @return True if the method handled the info, false if it didn't.
         * Returning false, or not having an OnErrorListener at all, will cause
         * the info to be discarded.
         */
        public boolean onPlayEvent(PlayerManager mgr,EnumMmInterfaceNotifyType notify);
    }
    
    /**
     * Register setOnAudioEventListener(OnAudioEventListener listener), your
     * listener will be triggered when the events posted from native code.
     *
     * @param listener OnAudioEventListener
     */
    public void setOnPlayerEventListener(OnPlayerEventListener listener)
    {
        mOnEventListener = listener;
    }
    
    private class EventHandler extends Handler
    {
        private PlayerManager mMSrv;

        public EventHandler(PlayerManager srv, Looper looper)
        {
            super(looper);
            mMSrv = srv;
        }

        @Override
        public void handleMessage(Message msg)
        {
            System.out.println("PlayerManager.java line 216 ,handleMessage");
            if (mMSrv.mNativeContext == 0)
            {

                return;
            }
            PlayerManager.EVENT[] ev = PlayerManager.EVENT.values();

            switch (ev[msg.what])
            {
                case EVENT_FROM_NATIVE:
                    {
                        if (mOnEventListener != null)
                            mOnEventListener.onPlayEvent(mMSrv,(EnumMmInterfaceNotifyType)msg.obj);
                   }
                    return;
                default:
                    System.err.println("Unknown message type " + msg.what);
                    return;    
            }
        }
    }

    
    private static void postEventFromNative(Object srv_ref,int eCmd, int u32Param, int u32Info)
        {
            PlayerManager srv = (PlayerManager) ((WeakReference) srv_ref).get();
            if (srv == null)
            {
                return;
            }
            EnumMmInterfaceNotifyType notify  = EnumMmInterfaceNotifyType.values()[eCmd];
            
            if (srv.mEventHandler != null)
            {
                Message m = srv.mEventHandler.obtainMessage(EVENT.EVENT_FROM_NATIVE.ordinal(),notify);
                srv.mEventHandler.sendMessage(m);
            }
            return;
        }

    public native int setContentSource(String str);
    public native int init(Surface msurface);
    public native int deinit();
    public native int play(String url);
    public native int play_dianduuri(String url);
    public native int stop();
    public native int pause();
    public native int resume();
    public native int seekTo(int msec);
    public native int setPlayMode(int speed);
    public int setOption(EnumMmInterfaceOptionType eOption, int arg1 )
    {
        return setOption(eOption.ordinal(),arg1);
    }
    private native int setOption(int eOption,int arg1);
    public native int getDuration();
    public native int getPlayerTime();

    
}
