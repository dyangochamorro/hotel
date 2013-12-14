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
#define LOG_TAG "C_MM_Sample_JNI"
#include <utils/Log.h>
#include <stdio.h>
#include <utils/threads.h>

#include "jni.h"
#include "JNIHelp.h"
#include "android_runtime/AndroidRuntime.h"
#include <surfaceflinger/Surface.h>  // for surface overlay
#include <gui/ISurfaceTexture.h>
#include "android_runtime/android_view_Surface.h"

#include "PlayerInterface.h"
       
// ----------------------------------------------------------------------------
         
using namespace android;

// ----------------------------------------------------------------------------

struct fields_t
{
    jfieldID    context;
    jfieldID    player;
    jmethodID   post_event;
    jfieldID    surface_texture;     // for surface overlay
};

jobject     mObject;
jclass      mClass;
static fields_t fields;
static Mutex sLock;

static bool bStopCalled = false;

class DemoDataSource: public BaseDataSource
{
public:
    DemoDataSource( );
    ~DemoDataSource();
    status_t initCheck();

    ssize_t readAt(off_t offset, void *data, size_t size);

    ssize_t readAt(void* data, size_t size);

    int seekAt(off_t offset);

    off_t tellAt();

    status_t getSize(off_t *size);
private:
    FILE *mpFile;
    long long  mFileLength;

};
DemoDataSource::DemoDataSource()
{
    mpFile = fopen("/mnt/usb/sda1/sohu.mp4","rb");
    if(mpFile == NULL)
    {
        LOGE("fopen file error\n");
    }
    mFileLength = 0;
}
DemoDataSource::~DemoDataSource()
{
    if(mpFile != NULL)
    {
        fclose(mpFile);
        mpFile = NULL;
    }
}

status_t DemoDataSource::initCheck()
{
    return OK;
}

ssize_t DemoDataSource::readAt(off_t offset, void *data, size_t size)
{
    return 0;
}

ssize_t DemoDataSource::readAt(void* data, size_t size)
{
    int len = 0;
    len = fread(data, 1, size, mpFile);
    return len;
}
int DemoDataSource:: seekAt(off_t offset)
{
   int ret =  fseek(mpFile,offset,SEEK_SET);
   LOGE("SeekAt %d\n", (int)offset);
   return ret;
}


off_t DemoDataSource::tellAt()
{
    unsigned long long  len = 0;
    len  = ftell(mpFile);
    LOGE("tellAt %d\n", (int)len);
    return (off_t)len;
}
status_t DemoDataSource::getSize(off_t *size)
{
    unsigned long long len = 0;
    if((mpFile != NULL) && (mFileLength ==0))
    {
        fseek(mpFile, 0, SEEK_END);
        len = ftell((FILE*)mpFile);
        fseek(mpFile, 0, SEEK_SET);
        mFileLength = len;
    }
    *size = mFileLength;
    LOGE("Len is %d\n", (int)*size);
    return OK;
}



// ----------------------------------------------------------------------------
// JNI functions
static sp<PlayerInterface> setPlayerManager(JNIEnv* env, jobject thiz, const sp<PlayerInterface>& srv)
{
    Mutex::Autolock l(sLock);
    sp<PlayerInterface> old = (PlayerInterface*)env->GetIntField(thiz, fields.context);
    if (srv.get())
    {
        srv->incStrong(thiz);
    }
    if (old != 0)
    {
        old->decStrong(thiz);
    }
    env->SetIntField(thiz, fields.context, (int)srv.get());
    return old;
}

static sp<PlayerInterface> getPlayerManager(JNIEnv* env, jobject thiz)
{
    Mutex::Autolock l(sLock);
    PlayerInterface* const p = (PlayerInterface*)env->GetIntField(thiz, fields.context);
    return sp<PlayerInterface>(p);
}

// ----------------------------------------------------------------------------

//-------------------------------------------------------------------------------
//callback funtion
void playerCallBack(EN_MM_INTERFACE_NOTIFY_TYPE eCmd, unsigned int u32Param, unsigned int u32Info)
{
    LOGE("enter playerCallBack..\n");
  
    JNIEnv *env = AndroidRuntime::getJNIEnv();
     env->CallStaticVoidMethod(mClass,fields.post_event,mObject,(int)eCmd,u32Param,u32Info);
    
        LOGE("leave playerCallBack...\n");

}


//end callback funtion
//-------------------------------------------------------------------------------




//-------------------------------------------------------------------------------
// for surface

static sp<ISurfaceTexture>
getVideoSurfaceTexture(JNIEnv* env, jobject thiz) {
    ISurfaceTexture * const p = (ISurfaceTexture*)env->GetIntField(thiz, fields.surface_texture);
    return sp<ISurfaceTexture>(p);
}

static void
decVideoSurfaceRef(JNIEnv *env, jobject thiz)
{
    //sp<MediaPlayer> mp = getMediaPlayer(env, thiz);
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    if (ms == NULL) {
        return;
    }
    LOGE("....aaa\n");

    sp<ISurfaceTexture> old_st = getVideoSurfaceTexture(env, thiz);
        LOGE("....bbb\n");

    if (old_st != NULL) {
            LOGE("....ccc\n");

        old_st->decStrong(thiz);
            LOGE("....ddd\n");

    }
}

static int
setVideoSurface(JNIEnv *env, jobject thiz, jobject jsurface, jboolean mediaPlayerMustBeAlive)
{
//    sp<MediaPlayer> mp = getMediaPlayer(env, thiz);
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    if (ms == NULL) {
        if (mediaPlayerMustBeAlive) {
            jniThrowException(env, "java/lang/IllegalStateException", NULL);
        }
        return -1;
    }
    LOGE("....0\n");

   // return 1;
    decVideoSurfaceRef(env, thiz);
    LOGE("....1\n");
    sp<ISurfaceTexture> new_st;
    if (jsurface) {
        sp<Surface> surface(Surface_getSurface(env, jsurface));
            LOGE("....2\n");

        if (surface != NULL) {
                LOGE("........3\n");

                new_st = surface->getSurfaceTexture();
                LOGE("....4\n");

            new_st->incStrong(thiz);
                LOGE("....5\n");

        } else {
            jniThrowException(env, "java/lang/IllegalArgumentException",
                    "The surface has been released");
            return -1;
        }
    }
    LOGE("....6\n");

    env->SetIntField(thiz, fields.surface_texture, (int)new_st.get());

    // This will fail if the media player has not been initialized yet. This
    // can be the case if setDisplay() on MediaPlayer.java has been called
    // before setDataSource(). The redundant call to setVideoSurfaceTexture()
    // in prepare/prepareAsync covers for this case.
        LOGE("....7\n");

    ms->SetSurface(new_st);
        LOGE("....8\n");

    return 1;
}

//end for surface
//---------------------------------------------------------------------------------



    
//--------------------------------------------------------------------------------
//Java native futions implement
/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    native_setup
 * Signature: (Ljava/lang/Object;)V
 */
void  com_ics_mm_PlayerManager_native_init(JNIEnv * env, jclass thiz)
{
    jclass clazz = env->FindClass("com/ics/mm/PlayerManager");
    fields.context = env->GetFieldID(clazz, "mNativeContext", "I");
    fields.post_event = env->GetStaticMethodID(clazz, "postEventFromNative","(Ljava/lang/Object;III)V");
    fields.player = env->GetFieldID(clazz, "mPlayerManagerContext", "I");
    fields.surface_texture = env->GetFieldID(clazz, "mSurfaceTexture", "I");
}

/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    native_setup
 * Signature: (Ljava/lang/Object;)V
 */
void com_ics_mm_PlayerManager_native_setup(JNIEnv * env, jobject thiz , jobject weak_this)
{
    LOGI(" native_setup \n");
    #if 0
    sp<PlayerInterface> srv = new PlayerInterface();
    if (srv == NULL)
    {
        jniThrowException(env, "java/lang/RuntimeException", "can't connect to audiomanager server.please check tvos server \n");
        return;
    }
    #endif
   jclass jclass_PlayerManager = env->FindClass("com/ics/mm/PlayerManager");
    mClass  = (jclass)env->NewGlobalRef(jclass_PlayerManager);
    mObject  = env->NewGlobalRef(weak_this);
    
    #if 0
    srv->RegisterCallBack(playerCallBack);
    setPlayerManager(env, thiz, srv);
    #endif
    
}

/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    native_finalize
 * Signature: ()V
 */
void com_ics_mm_PlayerManager_native_finalize(JNIEnv * env, jobject thiz)
{
    LOGI("native_finalize \n");
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
 

    
}

/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    setContentSource
 * Signature: ()I
 */
jint com_ics_mm_PlayerManager_setContentSource(JNIEnv * env, jobject thiz)
{
    LOGI("setContentSource \n");

    sp<PlayerInterface> srv = new PlayerInterface();
    if (srv == NULL)
    {
        jniThrowException(env, "java/lang/RuntimeException", "can't connect to audiomanager server.please check tvos server \n");
        return 0;
    }
    srv->RegisterCallBack(playerCallBack);
    setPlayerManager(env, thiz, srv);

  
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    
    DemoDataSource *pDataSource = new DemoDataSource();
    ms->SetContentSource(pDataSource);

    return 0;
}


/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    init
 * Signature: ()I
 */
jint com_ics_mm_PlayerManager_init(JNIEnv * env, jobject thiz, jobject jsurface)
{
    LOGI("init \n");
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    if(ms == 0)
    {
        return 0;
    }
    
    return setVideoSurface(env, thiz, jsurface, true /* mediaPlayerMustBeAlive */);
    
}


/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    deinit
 * Signature: ()I
 */
jint com_ics_mm_PlayerManager_deinit(JNIEnv * env, jobject thiz)
{
    LOGI("deinit \n");
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    return 0;
}


/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    play
 * Signature: (Ljava/lang/String;)I
 */
jint com_ics_mm_PlayerManager_play(JNIEnv * env, jobject thiz, jstring url)
{
    LOGI("play \n");
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    
    char temp[200] = {'\0'}; 
    strcpy(temp,env->GetStringUTFChars(url,NULL));
    return ms->Play(temp);
}

/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    stop
 * Signature: ()I
 */
jint com_ics_mm_PlayerManager_stop(JNIEnv * env, jobject thiz)
{
    LOGI("stop \n");
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    if(ms == 0)
    {
        return 0;
    }

     ms->Stop();

    //decVideoSurfaceRef(env, thiz);
    setPlayerManager(env, thiz, 0);

     return 0;

}

/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    pause
 * Signature: ()I
 */
jint com_ics_mm_PlayerManager_pause(JNIEnv * env, jobject thiz)
{
    LOGI("pause \n");
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    
    
    return ms->Pause();
}

/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    resume
 * Signature: ()I
 */
jint com_ics_mm_PlayerManager_resume(JNIEnv * env, jobject thiz)
{
    LOGI("resume \n");
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    if(ms == NULL)
    {
        return 0;
    }
    
    return ms->Resume();
}

/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    seekTo
 * Signature: ()I
 */
jint com_ics_mm_PlayerManager_seekTo(JNIEnv * env, jobject thiz, jint msec)
{
    LOGI("SeekTo \n");
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    
    return ms->SeekTo(msec);
}

/*
 * Class:     com_ics_mm_PlayerManager
 * Method:    setOption
 * Signature: ()I
 */
jint com_ics_mm_PlayerManager_setOption(JNIEnv * env, jobject thiz, jint eOption, jint arg1)
{
    LOGI("setOption \n");
    sp<PlayerInterface> ms = getPlayerManager(env, thiz);
    
    return ms->SetOption((EN_MM_INTERFACE_OPTOIN_TYPE)eOption, arg1);
}


//end JNI funtions
// ----------------------------------------------------------------------------







//------------------------------------------------------------------------------------
//Java VM system
static const char *classPathName = "com/ics/mm/PlayerManager";

static JNINativeMethod methods[] = {
  {"native_init",               "()V",           (void *)com_ics_mm_PlayerManager_native_init},
  {"native_setup",              "(Ljava/lang/Object;)V",    (void *)com_ics_mm_PlayerManager_native_setup},
  {"native_finalize",           "()V",          (void *)com_ics_mm_PlayerManager_native_finalize},
  {"setContentSource",          "()I",          (void *)com_ics_mm_PlayerManager_setContentSource},
  {"init",          "(Landroid/view/Surface;)I",(void *)com_ics_mm_PlayerManager_init},
  {"deinit",        "()I",                      (void *)com_ics_mm_PlayerManager_deinit},
  {"play",          "(Ljava/lang/String;)I",    (void *)com_ics_mm_PlayerManager_play},
  {"stop",          "()I",                      (void *)com_ics_mm_PlayerManager_stop},
  {"pause",         "()I",                      (void *)com_ics_mm_PlayerManager_pause},
  {"resume",        "()I",                      (void *)com_ics_mm_PlayerManager_resume},
  {"seekTo",        "(I)I",                     (void *)com_ics_mm_PlayerManager_seekTo},
  {"setOption",     "(II)I",                    (void *)com_ics_mm_PlayerManager_setOption},
};

/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv* env, const char* className,
    JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL)
    {
        LOGE("Native registration unable to find class '%s'", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0)
    {
        LOGE("RegisterNatives failed for '%s'", className);
        return JNI_FALSE;
    }
    return JNI_TRUE;
}

/*
 * Register native methods for all classes we know about.
 *
 * returns JNI_TRUE on success.
 */
static int registerNatives(JNIEnv* env)
{
  if (!registerNativeMethods(env, classPathName,methods, sizeof(methods) / sizeof(methods[0])))
  {
    return JNI_FALSE;
  }
  return JNI_TRUE;
}


// ----------------------------------------------------------------------------

/*
 * This is called by the VM when the shared library is first loaded.
 */

typedef union
{
    JNIEnv* env;
    void* venv;
} UnionJNIEnvToVoid;

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    UnionJNIEnvToVoid uenv;
    uenv.venv = NULL;
    jint result = -1;
    JNIEnv* env = NULL;

    LOGI("audiomanager JNI_OnLoad");

    if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_4) != JNI_OK)
    {
        LOGE("ERROR: GetEnv failed");
        goto bail;
    }
    env = uenv.env;

    if (registerNatives(env) != JNI_TRUE)
    {
        LOGE("ERROR: registerNatives failed");
        goto bail;
    }

    result = JNI_VERSION_1_4;

bail:
    return result;
}

//end java VM system




