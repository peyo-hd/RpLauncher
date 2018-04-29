package com.peyo.launcherlb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.HandlerThread;

public class LauncherModel {
    public interface Callbacks {
        void bindAllApplications(ArrayList<AppInfo> apps);
    }
    
    static final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
    static {
        sWorkerThread.start();
    }
    static final Handler sWorker = new Handler(sWorkerThread.getLooper());

    private final Context mContext;
    private Callbacks mCallbacks;
    private ArrayList<AppInfo> mAppList;
    DeferredHandler mHandler = new DeferredHandler();

    public LauncherModel(Context context, Callbacks callbacks) {
        mContext  = context;
        mAppList = new ArrayList<>();
        mCallbacks = callbacks;
    }

    public void startLoader() {
        sWorker.post(new Runnable() {
            @Override
            public void run() {
                getAppList();
                mHandler.post(new Runnable() {
                    public void run() {
                        if (mCallbacks != null) {
                            mCallbacks.bindAllApplications(mAppList);
                        }
                    }
                });
            }
        });
    }

    private static final String CATEGORY_LEANBACK_SETTINGS =
            "android.intent.category.LEANBACK_SETTINGS";

    private void getAppList() {
        mAppList.clear();
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> rInfos = pm.queryIntentActivities(
                new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0);
        for (ResolveInfo ri : rInfos) {
            AppInfo ai = new AppInfo(mContext, ri.activityInfo);
            mAppList.add(ai);
        }

        rInfos = pm.queryIntentActivities(
                new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER),0);
        for (ResolveInfo ri : rInfos) {
            AppInfo ai = new AppInfo(mContext, ri.activityInfo);
            addToAppList(ai);
        }

        rInfos = pm.queryIntentActivities(
                new Intent(Intent.ACTION_MAIN).addCategory(CATEGORY_LEANBACK_SETTINGS),0);
        for (ResolveInfo ri : rInfos) {
            AppInfo ai = new AppInfo(mContext, ri.activityInfo);
            if (!ai.getComponentName().getClassName().contains("NetworkActivity")) {
                mAppList.add(ai);
            }
        }
    }

    private void addToAppList(AppInfo info) {
        for (AppInfo ai: mAppList) {
            if (info.getComponentName().equals(ai.getComponentName())) {
                return;
            }
        }
        mAppList.add(info);
    }
}
