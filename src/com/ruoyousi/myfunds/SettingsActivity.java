package com.ruoyousi.myfunds;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ruoyousi.R;

public class SettingsActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	   addPreferencesFromResource(R.xml.settings);
	}

}