package com.peyo.rplauncher

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class AppInfo internal constructor(context: Context, private val mActivityInfo: ActivityInfo) {

    private val mPm: PackageManager
    val componentName: ComponentName

    val label: CharSequence?
        get() = mActivityInfo.loadLabel(mPm)

    val icon: Drawable?
        get() = mActivityInfo.loadIcon(mPm)

    val banner: Drawable?
        get() = mActivityInfo.loadBanner(mPm)

    init {
        mPm = context.packageManager
        componentName = ComponentName(mActivityInfo.packageName, mActivityInfo.name)
    }

}
