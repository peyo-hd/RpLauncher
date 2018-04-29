package com.peyo.launcherlb;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppInfo {

    private final PackageManager mPm;
    private final ActivityInfo mActivityInfo;
    private final ComponentName mComponentName;

    AppInfo(Context context, ActivityInfo info) {
        mPm = context.getPackageManager();
        mActivityInfo = info;
        mComponentName = new ComponentName(info.packageName, info.name);
    }

    public  CharSequence getLabel() {
        return mActivityInfo.loadLabel(mPm);
    }

    public Drawable getIcon() {
        return mActivityInfo.loadIcon(mPm);
    }

    public Drawable getBanner() {
        return mActivityInfo.loadBanner(mPm);
    }

    public ComponentName getComponentName() {
        return mComponentName;
    }

}
