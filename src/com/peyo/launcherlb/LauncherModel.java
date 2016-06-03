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
    static final String TAG = "LauncherModel";
    public interface Callbacks {
        public void bindAllApplications(ArrayList<LauncherActivityInfo> apps);
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
	
	public LauncherModel(LauncherAppState appState) {
		Context context = appState.getContext();
        mLauncherApps = (LauncherApps) context.getSystemService("launcherapps");
        mUserManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        mAppList = new ArrayList<LauncherActivityInfo>();
	}

	public void initialize(Callbacks callbacks) {
		mCallbacks = callbacks;
	}
	
	public void startLoader() {
		mLoaderTask = new LoaderTask();
        sWorker.post(mLoaderTask);
	}
	
	private LoaderTask mLoaderTask;
    private class LoaderTask implements Runnable {
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
    }
}
