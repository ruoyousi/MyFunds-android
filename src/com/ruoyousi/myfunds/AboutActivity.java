package com.ruoyousi.myfunds;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ruoyousi.R;
import com.ruoyousi.common.io.FileUtil;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		TextView tv = (TextView) findViewById(R.id.AboutText);
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(getString(R.string.msg_about_developer),
				getString(R.string.msg_about_developer_value));
		map.put(getString(R.string.msg_about_contact),
				getString(R.string.msg_about_contact_value));
		map.put(getString(R.string.msg_about_version_name), getVersionName());
		map.put(getString(R.string.msg_about_version_code), ""
				+ getVersionCode());
		populateField(map, tv);

		Button terms = (Button) findViewById(R.id.TermsButton);
		terms.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						AboutActivity.this);
				builder.setTitle(R.string.msg_about_terms);
				builder.setMessage(FileUtil.readFile(AboutActivity.this
						.getResources().openRawResource(R.raw.terms)));
				builder.create().show();
			}
		});
		Button privacy = (Button) findViewById(R.id.PrivacyButton);
		privacy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						AboutActivity.this);
				builder.setTitle(R.string.msg_about_privacy);
				builder.setMessage(FileUtil.readFile(AboutActivity.this
						.getResources().openRawResource(R.raw.privacy)));
				builder.create().show();
			}
		});

		Button feedback = (Button) findViewById(R.id.FeedbackButton);
		feedback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW, Uri
						.parse(getString(R.string.msg_about_feedback_url)));
				AboutActivity.this.startActivity(i);
			}
		});
	}

	private void populateField(Map<String, String> values, TextView view) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : values.entrySet()) {
			String fieldName = entry.getKey();
			String fieldValue = entry.getValue();
			sb.append(fieldName).append(": ").append("<b>").append(fieldValue)
					.append("</b><br>");
		}
		view.setText(Html.fromHtml(sb.toString()));
	}

	private String getVersionName() {
		String version = "";
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			version = "Package name not found";
		}
		return version;
	}

	private int getVersionCode() {
		int version = -1;
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version = pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
		}
		return version;
	}
}