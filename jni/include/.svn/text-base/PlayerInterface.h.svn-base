//<MStar Software>
//***********************************************************************************
//MStar Software
//Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
//All software, firmware and related documentation herein (“MStar Software”) are intellectual property of MStar Semiconductor, Inc. (“MStar”) and protected by law, including, but not limited to, copyright law and international treaties.  Any use, modification, reproduction, retransmission, or republication of all or part of MStar Software is expressly prohibited, unless prior written permission has been granted by MStar. 
//By accessing, browsing and/or using MStar Software, you acknowledge that you have read, understood, and agree, to be bound by below terms (“Terms”) and to comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar Software and any modification/derivatives thereof.  No right, ownership, or interest to MStar Software and any modification/derivatives thereof is transferred to you under Terms.
// 2. You understand that MStar Software might include, incorporate or be supplied together with third party’s software and the use of MStar Software may require additional licenses from third parties.  Therefore, you hereby agree it is your sole responsibility to separately obtain any and all third party right and license necessary for your use of such third party’s software. 
// 3. MStar Software and any modification/derivatives thereof shall be deemed as MStar’s confidential information and you agree to keep MStar’s confidential information in strictest confidence and not disclose to any third party.  
// 4. MStar Software is provided on an “AS IS” basis without warranties of any kind. Any warranties are hereby expressly disclaimed by MStar, including without limitation, any warranties of merchantability, non-infringement of intellectual property rights, fitness for a particular purpose, error free and in conformity with any international standard.  You agree to waive any claim against MStar for any loss, damage, cost or expense that you may incur related to your use of MStar Software.  In no event shall MStar be liable for any direct, indirect, incidental or consequential damages, including without limitation, lost of profit or revenues, lost or damage of data, and unauthorized system use.  You agree that this Section 4 shall still apply without being affected even if MStar Software has been modified by MStar in accordance with your request or instruction for your use, except otherwise agreed by both parties in writing.
// 5. If requested, MStar may from time to time provide technical supports or services in relation with MStar Software to you for your use of MStar Software in conjunction with your or your customer’s product (“Services”).  You understand and agree that, except otherwise agreed by both parties in writing, Services are provided on an “AS IS” basis and the warranty disclaimer set forth in Section 4 above shall apply.  
// 6. Nothing contained herein shall be construed as by implication, estoppels or otherwise: (a) conferring any license or right to use MStar name, trademark, service mark, symbol or any other identification; (b) obligating MStar or any of its affiliates to furnish any person, including without limitation, you and your customers, any assistance of any kind whatsoever, or any information; or (c) conferring any license or right under any intellectual property right.
// 7. These terms shall be governed by and construed in accordance with the laws of Taiwan, R.O.C., excluding its conflict of law rules.  Any and all dispute arising out hereof or related hereto shall be finally settled by arbitration referred to the Chinese Arbitration Association, Taipei in accordance with the ROC Arbitration Law and the Arbitration Rules of the Association by three (3) arbitrators appointed in accordance with the said Rules.  The place of arbitration shall be in Taipei, Taiwan and the language shall be English.  The arbitration award shall be final and binding to both parties.
//***********************************************************************************
//<MStar Software>
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef ANDROID_PlAYERINTERFACE_H
#define ANDROID_PlAYERINTERFACE_H

#include <binder/IMemory.h>
#include <media/IMediaPlayerClient.h>
#include <media/IMediaPlayer.h>
#include <media/IMediaDeathNotifier.h>
#include <media/IStreamSource.h>

#include <utils/KeyedVector.h>
#include <utils/String8.h>
#include <media/mediaplayer.h>
class ANativeWindow;

namespace android {

#define MM_INTERFACE_THREAD_SLEEP_MS	30000

typedef enum
{
    E_MM_INTERFACE_EXIT_OK = 0, //playback ok, and exit to ap
    E_MM_INTERFACE_EXIT_ERROR, //playback  error, and exit to ap
    E_MM_INTERFACE_SUFFICIENT_DATA, //when playing, data enough to continue play, and codec wil resume
    E_MM_INTERFACE_INSUFFICIENT_DATA, //when playing, run short of data, and codec wil pause

    E_MM_INTERFACE_START_PLAY, //player init ok, and start playing
    E_MM_INTERFACE_NULL, //playback notify null
} EN_MM_INTERFACE_NOTIFY_TYPE;

typedef enum
{
    E_MM_INTERFACE_STATE_NULL = 0,  
    E_MM_INTERFACE_STATE_WAIT_INIT,  
    E_MM_INTERFACE_STATE_INIT_DONE,  
    E_MM_INTERFACE_STATE_PLAYING,  
    E_MM_INTERFACE_STATE_PAUSE,  
    E_MM_INTERFACE_STATE_PLAY_DONE,  

} EN_MM_INTERFACE_PLAYING_STATE;

typedef enum
{
    E_MM_INTERFACE_OPTION_NULL = 0,
    E_MM_INTERFACE_OPTION_SET_STARTTIME,
    E_MM_INTERFACE_OPTION_SET_TOTAL_TIME,
    E_MM_INTERFACE_OPTION_RESET_BUF,
    E_MM_INTERFACE_OPTION_ENABLE_SEAMLESS,
    E_MM_INTERFACE_OPTION_CHANGE_PROGRAM,
}EN_MM_INTERFACE_OPTOIN_TYPE;


typedef void (*CmdCallback) (EN_MM_INTERFACE_NOTIFY_TYPE eCmd, unsigned int u32Param, unsigned int u32Info);//the callback 

class PlayerInterface;
class InterfaceListener: public MediaPlayerListener
{
public: 
	InterfaceListener( PlayerInterface* Player);
	~InterfaceListener( );
	void notify(int msg, int ext1, int ext2, const Parcel *obj);
	
private:
	PlayerInterface*mPlayer;
};


class PlayerInterface : public MediaPlayer 
{
public:
	PlayerInterface();
	~PlayerInterface();

	void RegisterCallBack(CmdCallback pCmdCbFunc);
   
	status_t SetContentSource(sp <BaseDataSource> DataSource);
	status_t Init(const sp<ISurfaceTexture>& surfaceTexture);
	status_t Deinit();
	status_t Play(char* url);
	status_t Stop();
	status_t Pause();
	status_t Resume();
	status_t SeekTo(int msec);
	status_t SetOption(EN_MM_INTERFACE_OPTOIN_TYPE eOption, int arg1 );
	status_t _StateProcess();
	CmdCallback mCallback;
	EN_MM_INTERFACE_PLAYING_STATE mePlayingState;


	class MonitorThread : public Thread
 	{
    	public:
        	MonitorThread(PlayerInterface *player):Thread(false),mPlayer(player){}
        	virtual status_t readyToRun();
        	virtual bool threadLoop();
    	protected:
         	~MonitorThread() {requestExit();}
    	private:
        	PlayerInterface *mPlayer;
    	};


	
private:
	sp<ISurfaceTexture>  msurfaceTexture;
	sp<InterfaceListener>  mListener;
	sp<MonitorThread> mThread;
	mutable Mutex mLock;

};


}; // namespace android

#endif // ANDROID_PlAYERINTERFACE_H
