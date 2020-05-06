package com.arpi.rplauncher

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class AppInfo internal constructor(context: Context, private val mActivityInfo: ActivityInfo) {

    private val mPm: PackageManager = context.packageManager
    val componentName: ComponentName = ComponentName(mActivityInfo.packageName, mActivityInfo.name)

    val label: CharSequence
        get() = mActivityInfo.loadLabel(mPm)

    val icon: Drawable?
        get() = mActivityInfo.loadIcon(mPm)

    val banner: Drawable?
        get() = mActivityInfo.loadBanner(mPm)

}
