package com.ap.lockscreen;

import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class LockScreenWidget extends AppWidgetProvider {

    private static final String LOCK = "lock";
    private boolean lock;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String data = bundle.getString(LOCK);
            if (data != null && data.equals(LOCK)) {
                L.i("Should lock");
                try {

                    DevicePolicyManager deviceManger = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                    deviceManger.lockNow();

                } catch (Exception ex) {
                    Toast.makeText(context, R.string.widget_ask_admin, Toast.LENGTH_LONG).show();
                    L.e("Error", ex);
                }
            } else {
                L.i("Shouldn't lock");
            }

        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.lock_screen_widget);

            Intent intent = new Intent(context, LockScreenWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            intent.putExtra(LOCK, LOCK);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

