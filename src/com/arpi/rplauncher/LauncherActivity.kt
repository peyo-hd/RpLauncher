package com.arpi.rplauncher

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityOptions
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
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
                "tv_user_setup_complete", 1)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F6) {
            val intent = Intent(Intent.ACTION_MAIN)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setComponent(GridFragment.selectedComponent)

            val am = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            if (am.isActivityStartAllowedOnDisplay(this, 1, intent)) {
                val bundle = ActivityOptions.makeBasic()
                    .setLaunchDisplayId(1).toBundle()
                this.startActivity(intent, bundle)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
