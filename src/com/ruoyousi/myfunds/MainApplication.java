package com.ruoyousi.myfunds;

import android.app.Application;
import android.preference.PreferenceManager;

import com.ruoyousi.R;

public class MainApplication extends Application {

	@Override
	public void onCreate() {
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
	}

	@Override
	public void onTerminate() {
	}
}
