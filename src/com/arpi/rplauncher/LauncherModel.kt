package com.arpi.rplauncher

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import java.util.ArrayList

class LauncherModel(private val mContext: Context, private val mCallbacks: Callbacks?) {
    private val mAppList: ArrayList<AppInfo> = ArrayList()
    private var mHandler = DeferredHandler()

    interface Callbacks {
        fun bindAllApplications(apps: ArrayList<AppInfo>)
    }

    fun startLoader() {
        sWorker.post {
            getAppList()
            mHandler.post(Runnable {
                mCallbacks?.bindAllApplications(mAppList)
            })
        }
    }

    private fun getAppList() {
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

    companion object {

        private val sWorkerThread = HandlerThread("launcher-loader")

        init {
            sWorkerThread.start()
        }

        internal val sWorker = Handler(sWorkerThread.looper)

        private const val LEANBACK_SETTINGS_PACKAGE = "com.android.tv.settings"
        private const val CATEGORY_LEANBACK_SETTINGS = "android.intent.category.LEANBACK_SETTINGS"
    }
}