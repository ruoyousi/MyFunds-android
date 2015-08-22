package com.ruoyousi.myfunds;

import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.ruoyousi.R;
import com.ruoyousi.common.util.MathUtil;
import com.ruoyousi.myfunds.shared.FormatUtils;
import com.ruoyousi.myfunds.shared.UserFundsSummary;
import com.ruoyousi.myfunds.shared.UserFundData;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String TAG = MyWidgetProvider.class.getName();

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d(TAG, "onUpdate");

		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.d(TAG, "onDeleted");
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.d(TAG, "onEnabled");

		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName("com.ruoyousi",
				".myfunds.MyFundsBroadcastReceiver"),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Log.d(TAG, "onDisabled");

		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName("com.ruoyousi",
				".myfunds.MyFundsBroadcastReceiver"),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId);

		DbAdapter db = new DbAdapter(context);
		List<UserFundData> funds = db.loadAllUserFund();
		UserFundsSummary summary = DbAdapter.sumUserFund(funds);
		db.close();

		CharSequence summaryText = context.getString(R.string.userFund_title,
				FormatUtils.formatMoney(summary.getTotalPrincial()),
				FormatUtils.formatMoney(summary.getTotalMoney()), FormatUtils
						.formatEarningRate(MathUtil.growthRate(summary
								.getTotalMoney(), summary.getTotalPrincial())));

		Log.d(TAG, "updateAppWidget text=" + summaryText);

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_provider);
		views.setTextViewText(R.id.appwidget_text, summaryText);

		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		views.setOnClickPendingIntent(R.id.appwidget_title, pendingIntent);
		views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

		appWidgetManager.updateAppWidget(new ComponentName(context,
				MyWidgetProvider.class), views);

		savePref(context, appWidgetId);
	}

	private static final String PREFS_NAME = "com.ruoyousi.myfunds.MyWidgetProvider";
	private static final String PREF_PREFIX_KEY = "prefix_";

	static void savePref(Context context, int appWidgetId) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		prefs.putInt(PREF_PREFIX_KEY + appWidgetId, appWidgetId);
		prefs.commit();
	}
}
