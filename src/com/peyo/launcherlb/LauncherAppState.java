package com.peyo.launcherlb;

import android.content.Context;
import android.util.Log;

public class LauncherAppState {
    static final String TAG = "LauncherAppState";

    private static Context sContext;
	private static LauncherAppState INSTANCE;
    
	public static void setApplicationContext(Context context) {
        if (sContext != null) {
            Log.w(TAG, "setApplicationContext called twice! old=" + sContext + " new=" + context);
        }
        sContext = context.getApplicationContext();
	}

	public static LauncherAppState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LauncherAppState();
        }
        return INSTANCE;
	}

	private LauncherModel mModel;
	
    private LauncherAppState() {
        if (sContext == null) {
            throw new IllegalStateException("LauncherAppState inited before app context set");
        }
        Log.v(TAG, "LauncherAppState inited");
        mModel = new LauncherModel(this);
    }
    
    LauncherModel setLauncher(Launcher launcher) {
        mModel.initialize(launcher);
        return mModel;
    }

	public Context getContext() {
		return sContext;
	}
}
