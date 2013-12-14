LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := \
    ./com_ics_mm_PlayerManager.cpp
LOCAL_C_INCLUDES := \
    $(JNI_H_INCLUDE) \
    $(LOCAL_PATH)/include
LOCAL_SHARED_LIBRARIES := \
    libandroid_runtime \
    libnativehelper \
    libcutils \
    libutils \
    libbinder \
    libfbdev \
    libmedia\
    libgui
LOCAL_MODULE_PATH:= $(LOCAL_PATH)/libmm_jni
#LOCAL_CFLAGS := -DMSOS_TYPE_LINUX
LOCAL_CFLAGS += -DUSE_ANDROID_OVERLAY
LOCAL_CFLAGS += -DAndroid_4
LOCAL_MODULE := libmm_player_jni
LOCAL_MODULE_TAGS := optional
LOCAL_PRELINK_MODULE := false
include $(BUILD_SHARED_LIBRARY)