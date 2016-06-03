package com.peyo.launcherlb;

import java.util.ArrayList;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.pm.LauncherActivityInfo;
import android.os.Bundle;

public class Launcher extends Activity implements LauncherModel.Callbacks {
	private LauncherModel mModel;
    private GridFragment mFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            mFragment = new GridFragment();
            transaction.replace(R.id.grid_fragment, mFragment);
            transaction.commit();
        }
        LauncherAppState.setApplicationContext(getApplicationContext());
        LauncherAppState appState = LauncherAppState.getInstance();
        mModel = appState.setLauncher(this);
        mModel.startLoader();
	}

	@Override
	public void bindAllApplications(ArrayList<LauncherActivityInfo> apps) {
		mFragment.updateApps(apps);
	}
}
