////IShineSystemManage.aidl
////version 0.1
////author: byh
////time 2013-04-15 14:00

package com.shine.systemmanage.aidl;
interface IShineSystemManage
{
////tts///////////////////////////////////////////////////
////actionMode 0£º startNewTTS  1£º stop  2 Volume 
	int TTSChannel1(int nActionMode, String strValue);
	int TTSChannel2(int nActionMode, String strValue);
////screenshot//////////////////////////	
	int ScreenShot(String strStorePath, int w, int h);

////power, rtc///////////////////////////////////////////////////
	int Shutdown();
	String GetPowerWakeTime();
	int SetPowerWakeTime(String strTime);//"2013-04-15 13:58:00"
	String GetRtcTime();
	int SetRtcTime(String strTime);//"2013-04-15 13:58:00"
////display///////////////////////////////////////////////////
	int EnableDisplay();
	int DisableDisplay();
	int GetDisplayState();

////goio pin5, 6; setMode getValue setValue	nMode: 0 Out 1 In////////
	int GPIOGetMode();
	int GPIOSetMode(int nPin, int nMode);
	int GPIOGetValue(int nPin);
	int GPIOSetValue(int nPin, int nVal);

////panel///////////////////////////////////////////////////	
////back light 0-255
	int GetPanelBackLight();
	int SetPanelBackLight(int nVal);	
////mirror default 0, 1 : ratate 180 degre
	int GetPanelMirrorState();
	int SetPanelMirror(int nMirror);
////solution	
    int SetPanelSolution(int w, int h);	
    String GetPanelSolution();//1024x768
////oddEven  default 0; 
    int SwitchPanelOddEven(int nSwitch);	
    int GetPanelOddEventState();	
}