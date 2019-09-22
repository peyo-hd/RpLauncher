package com.arpi.rplauncher

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import java.util.ArrayList

class LauncherActivity : Activity(), LauncherModel.Callbacks {
    private lateinit var mModel: LauncherModel
    private lateinit var mFragment: GridFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            mFragment = GridFragment()
            fragmentManager.beginTransaction()
                    .replace(R.id.grid_fragment, mFragment)
                    .commit()
        }
        mModel = LauncherModel(applicationContext, this)
        mModel.startLoader()
    }

    private val mPackageListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mModel.startLoader()
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mPackageListener,
                IntentFilter().apply {
                    addAction(Intent.ACTION_PACKAGE_ADDED)
                    addAction(Intent.ACTION_PACKAGE_CHANGED)
                    addAction(Intent.ACTION_PACKAGE_REMOVED)
                    addAction(Intent.ACTION_PACKAGE_REPLACED)
                    addDataScheme("package")
                }
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mPackageListener)
    }

    override fun bindAllApplications(apps: ArrayList<AppInfo>) {
        mFragment.updateApps(apps)

        Settings.Secure.putInt(getContentResolver(),
                Settings.Secure.TV_USER_SETUP_COMPLETE, 1)
    }
}
