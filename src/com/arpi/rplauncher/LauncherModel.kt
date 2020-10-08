package com.arpi.rplauncher

import android.content.Context
import android.content.Intent
import java.util.ArrayList

class LauncherModel(private val mContext: Context) {
    private val mAppList: ArrayList<AppInfo> = ArrayList()

    suspend fun getAppList(): ArrayList<AppInfo> {
        return mAppList
    }

    suspend fun loadAppList() {
        mAppList.clear()
        val pm = mContext.packageManager
        var rInfos = pm.queryIntentActivities(
                Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0)
        for (ri in rInfos) {
            val ai = AppInfo(mContext, ri.activityInfo)
            addToAppList(ai)
        }

        rInfos = pm.queryIntentActivities(
                Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER), 0)
        for (ri in rInfos) {
            val ai = AppInfo(mContext, ri.activityInfo)
            addToAppList(ai)
        }

        rInfos = pm.queryIntentActivities(
                Intent(Intent.ACTION_MAIN).addCategory(CATEGORY_LEANBACK_SETTINGS), 0)
        for (ri in rInfos) {
            val ai = AppInfo(mContext, ri.activityInfo)
            if (!ai.componentName.className.contains("NetworkActivity")) {
                mAppList.add(ai)
            }
        }
    }

    private fun addToAppList(info: AppInfo) {
        if (info.componentName.packageName.equals(LEANBACK_SETTINGS_PACKAGE))
            return

        for (ai in mAppList) {
            if (info.componentName == ai.componentName) {
                return
            }
        }
        mAppList.add(info)
    }

    private val LEANBACK_SETTINGS_PACKAGE = "com.android.tv.settings"
    private val CATEGORY_LEANBACK_SETTINGS = "android.intent.category.LEANBACK_SETTINGS"
}
