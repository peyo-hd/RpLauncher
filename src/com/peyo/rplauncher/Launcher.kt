package com.peyo.rplauncher

import android.app.Activity
import android.os.Bundle
import java.util.ArrayList

class Launcher : Activity(), LauncherModel.Callbacks {
    lateinit var mModel: LauncherModel
    lateinit var mFragment: GridFragment

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

    override fun bindAllApplications(apps: ArrayList<AppInfo>) {
        mFragment.updateApps(apps)
    }
}
