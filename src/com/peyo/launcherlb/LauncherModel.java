package com.peyo.launcherlb;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.os.Handler;
import android.os.HandlerThread;

import android.os.UserHandle;
import android.os.UserManager;

public class LauncherModel {
    public interface Callbacks {
        void bindAllApplications(ArrayList<LauncherActivityInfo> apps);
    }
    
    static final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
    static {
        sWorkerThread.start();
    }
    static final Handler sWorker = new Handler(sWorkerThread.getLooper());

	private Callbacks mCallbacks;
	private ArrayList<LauncherActivityInfo> mAppList;
	private LauncherApps mLauncherApps;
	private UserManager mUserManager;
	DeferredHandler mHandler = new DeferredHandler();
	
	public LauncherModel(Context context, Callbacks callbacks) {
        mLauncherApps = (LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE);
        mUserManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        mAppList = new ArrayList<>();
        mCallbacks = callbacks;
	}

	public void startLoader() {
        sWorker.post(new Runnable() {
			@Override
			public void run() {
				final List<UserHandle> profiles = mUserManager.getUserProfiles();
				mAppList.clear();
				for (UserHandle user : profiles) {
					final List<LauncherActivityInfo> apps = mLauncherApps.getActivityList(null, user);
					mAppList.addAll(apps);
				}
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
}
