// Eddy Davila
// MDF 3
// Week 3
// Full Sail University

package com.davila.widget.android.androidwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.davila.widget.android.androidwidget.storage.Record;

public class RecordsWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_VIEW_DETAILS = "com.davila.widget.android.ACTION_VIEW_DETAILS";
    public static final String ACTION_ADDED_NEW = "com.davila.widget.android.ACTION_ADDED_NEW";
    public static final String EXTRA_ITEM = "com.davila.widget.android.RecordsWidgetProvider.EXTRA_ITEM";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {

            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, RecordsWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            widgetView.setRemoteAdapter(R.id.article_list, intent);
            widgetView.setEmptyView(R.id.article_list, R.id.empty);
            // Create an Intent to launch ExampleActivity
            Intent intent2 = new Intent(context, NewRecordActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
            widgetView.setOnClickPendingIntent(R.id.btn_add_new, pendingIntent);

            Intent detailIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            widgetView.setPendingIntentTemplate(R.id.article_list, pIntent);

            //			ComponentName component=new ComponentName(context,WidgetTaskSchedular.class);
            //			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.article_list);
            //			appWidgetManager.updateAppWidget(component, remoteView);
            //			appWidgetManager.updateAppWidget(widgetId, widgetView);
            appWidgetManager.updateAppWidget(widgetId, widgetView);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_VIEW_DETAILS)) {
            Record record = (Record) intent.getSerializableExtra(EXTRA_ITEM);
            Intent details = new Intent(context, ViewRecordActivity.class);
            details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            details.putExtra("view_record", record);
            context.startActivity(details);
        } else if (intent.getAction().equals(ACTION_ADDED_NEW)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, RecordsWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.article_list);
        }

        super.onReceive(context, intent);
    }

}
