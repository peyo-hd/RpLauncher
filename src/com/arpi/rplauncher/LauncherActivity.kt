package com.arpi.rplauncher

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LauncherActivity : Activity() {
    private lateinit var mModel: LauncherModel
    private var mFragment: GridFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            mFragment = GridFragment()
            fragmentManager.beginTransaction()
                    .replace(R.id.grid_fragment, mFragment)
                    .commit()
        }

        mModel = LauncherModel(applicationContext)
        refreshAppList()

        registerReceiver(mPackageListener,
                IntentFilter().apply {
                    addAction(Intent.ACTION_PACKAGE_ADDED)
                    addAction(Intent.ACTION_PACKAGE_CHANGED)
                    addAction(Intent.ACTION_PACKAGE_REMOVED)
                    addAction(Intent.ACTION_PACKAGE_REPLACED)
                    addDataScheme("package")
                }
        )

        Settings.Secure.putInt(getContentResolver(),
                Settings.Secure.TV_USER_SETUP_COMPLETE, 1)
    }

    private val mPackageListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            refreshAppList()
        }
    }

    private val appListMutex = Mutex()

    private fun refreshAppList() {
        GlobalScope.launch {
            appListMutex.withLock {
                mModel.loadAppList()
                mFragment?.updateApps(mModel.getAppList())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mPackageListener)
    }
}
