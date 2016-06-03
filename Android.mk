LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := LauncherLB
LOCAL_MODULE_TAGS := optional

LOCAL_STATIC_JAVA_LIBRARIES := \
    android-support-v4 \
    android-support-v7-recyclerview \
    android-support-v17-leanback

LOCAL_RESOURCE_DIR := \
    $(TOP)/frameworks/support/v17/leanback/res \
    $(TOP)/frameworks/support/v7/recyclerview/res \
    $(LOCAL_PATH)/res \

LOCAL_AAPT_FLAGS := \
    --auto-add-overlay \
    --extra-packages android.support.v17.leanback:android.support.v7.recyclerview

LOCAL_SRC_FILES := $(call all-java-files-under, src)

include $(BUILD_PACKAGE)
