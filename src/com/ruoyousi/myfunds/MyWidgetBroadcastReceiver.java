package com.ruoyousi.myfunds;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyWidgetBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("MyFundsBroadcastReceiver", "intent=" + intent);

		String action = intent.getAction();
		if (action.equals(Intent.ACTION_TIMEZONE_CHANGED)
				|| action.equals(Intent.ACTION_TIME_CHANGED)) {
			AppWidgetManager gm = AppWidgetManager.getInstance(context);
			int[] appWidgetIds = gm.getAppWidgetIds(new ComponentName(context,
					MyWidgetProvider.class));
			final int N = appWidgetIds.length;
			for (int i = 0; i < N; i++) {
				MyWidgetProvider.updateAppWidget(context, gm, appWidgetIds[i]);
			}
		}
	}

}
