package com.peyo.rplauncher

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import java.util.ArrayList

class LauncherModel(private val mContext: Context, private val mCallbacks: Callbacks?) {
    private val mAppList: ArrayList<AppInfo>
    internal var mHandler = DeferredHandler()

    interface Callbacks {
        fun bindAllApplications(apps: ArrayList<AppInfo>)
    }

    init {
        mAppList = ArrayList()
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
            mAppList.add(ai)
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
        for (ai in mAppList) {
            if (info.componentName == ai.componentName) {
                return
            }
        }
        mAppList.add(info)
    }

    companion object {

        internal val sWorkerThread = HandlerThread("launcher-loader")

        init {
            sWorkerThread.start()
        }

        internal val sWorker = Handler(sWorkerThread.looper)

        private val CATEGORY_LEANBACK_SETTINGS = "android.intent.category.LEANBACK_SETTINGS"
    }
}