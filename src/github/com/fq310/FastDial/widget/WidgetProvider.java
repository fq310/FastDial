package github.com.fq310.FastDial.widget;

import github.com.fq310.FastDial.git.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	public static final String NAME = "name";
	public static final String NUMBER = "number";
	public static final String FORE_COLOR = "fore_color";
	public static final String BACK_COLOR = "back_color";
	public static final String FONT_SIZE = "size";
			
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            update(context, appWidgetId, appWidgetManager);
        }
	}
	
	public static void update(Context context, int appWidgetId, AppWidgetManager appWidgetManager) {
        SharedPreferences settings = context.getSharedPreferences(String.valueOf(appWidgetId), 0);
        if (settings == null) return;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		views.setTextViewText(R.id.textView_widget, settings.getString(NAME, ""));
		views.setTextColor(R.id.textView_widget, settings.getInt(FORE_COLOR, 0));
		views.setInt(R.id.textView_widget, "setBackgroundColor", settings.getInt(BACK_COLOR, 0));
		views.setFloat(R.id.textView_widget, "setTextSize", settings.getInt(FONT_SIZE, 40));

		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + settings.getString(NUMBER, "")));
		PendingIntent Pfullintent = PendingIntent.getActivity(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.textView_widget, Pfullintent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
	}
}
