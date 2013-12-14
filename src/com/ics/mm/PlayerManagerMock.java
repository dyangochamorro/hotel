package com.ics.mm;



import android.view.Surface;
import android.view.SurfaceHolder;

public class PlayerManagerMock {
    
    public enum EnumMmInterfaceNotifyType
    {
        E_MM_INTERFACE_EXIT_OK, //playback ok, and exit to ap
        E_MM_INTERFACE_EXIT_ERROR, //playback  error, and exit to ap
        E_MM_INTERFACE_SUFFICIENT_DATA, //when playing, data enough to continue play, and codec wil resume
        E_MM_INTERFACE_INSUFFICIENT_DATA, //when playing, run short of data, and codec wil pause
        E_MM_INTERFACE_START_PLAY, //player init ok, and start playing
        E_MM_INTERFACE_NULL, //playback notify null
    } 
    
    public PlayerManagerMock()
    {
        
    }
    public interface OnPlayerEventListener
    {

        public boolean onPlayEvent(PlayerManagerMock mgr,EnumMmInterfaceNotifyType notify);
    }
    
    public void setOnPlayerEventListener(OnPlayerEventListener listener)
    {
        
    }
    
    public  int setContentSource()
    {
        return 0;
    }
    
    public  int init(Surface surface)
    {
        return 0;
    }
    
    public  int deinit()
    {
        return 0;
    }
    
    public int play(String url)
    {
        return 0;
    }
    
    public int stop()
    {
        return 0;
    }
    
    public int pause()
    {
        return 0;
    }
    
    public int resume()
    {
        return 0;
    }
    
    public int seekTo(int msec)
    {
        return 0;
    }
    
    public void setDisplay(SurfaceHolder msurfacddhoder)
    {
        
    }
    

}
