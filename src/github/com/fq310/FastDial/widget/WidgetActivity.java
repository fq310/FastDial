package github.com.fq310.FastDial.widget;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerDialog.OnColorChangedListener;
import github.com.fq310.FastDial.git.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

public class WidgetActivity extends Activity {
	
	private static final int INITIAL_COLOR = 0xFFFF4444;
	private static final int DEFAULT_BACK_COLOR = 0xFF141414;
	private static final int DEFAULT_FORE_COLOR = 0xFFFF3030;
	private int foreColor = DEFAULT_FORE_COLOR;
	private int backColor = DEFAULT_BACK_COLOR;
	private int mAppWidgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget);
		setTextViewColor();
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    mAppWidgetId = extras.getInt(
		            AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		if (AppWidgetManager.INVALID_APPWIDGET_ID == mAppWidgetId) {
            setResult(RESULT_CANCELED);
            finish();
        }
	}

	private void setTextViewColor() {
		findViewById(R.id.textView_ForeColor).setBackgroundColor(foreColor);
		findViewById(R.id.textView_BackColor).setBackgroundColor(backColor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.widget, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addFromAddress(View view) {
		
	}
	
	private OnCancelListener colorPickerCancelListener = new OnCancelListener() {
		@Override
		public void onCancel(DialogInterface arg0) {
			foreColor = DEFAULT_FORE_COLOR;
			backColor = DEFAULT_BACK_COLOR;
			setTextViewColor();
		}
	};
	private ColorPickerDialog foreColorDialog;
	private ColorPickerDialog backColorDialog;
	
	public void onCancel(View v) {
		destroyColorPickerDialog();
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_CANCELED, resultValue);
		finish();
	}
	
	private void destroyColorPickerDialog() {
		if (foreColorDialog != null) foreColorDialog.dismiss();
		if (backColorDialog != null) backColorDialog.dismiss();
	}

	public void onOK(View v) {
		destroyColorPickerDialog();
		initialWidget();
		
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();
	}
	
	private void initialWidget() {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
		views.setTextViewText(R.id.textView_widget, getName());
		views.setTextColor(R.id.textView_widget, foreColor);
		views.setInt(R.id.textView_widget, "setBackgroundColor", backColor);
		views.setFloat(R.id.textView_widget, "setTextSize", 70);
		
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getNumber()));
		PendingIntent Pfullintent = PendingIntent.getActivity(this, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.textView_widget, Pfullintent);
		
		appWidgetManager.updateAppWidget(mAppWidgetId, views);
	}

	private String getName() {
		return getEditTextString(R.id.editText_name);
	}
	
	private String getNumber() {
		return getEditTextString(R.id.editText_number);
	}
	
	private String getEditTextString(int id) {
		return ((EditText) this.findViewById(id)).getText().toString().trim();
	}
	
	public void addForeColor(View view) {
		foreColorDialog = new ColorPickerDialog(this, INITIAL_COLOR);
		foreColorDialog.setAlphaSliderVisible(true);
		foreColorDialog.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				foreColor = color;
				setTextViewColor();
			}
		});
		foreColorDialog.setOnCancelListener(colorPickerCancelListener);
		foreColorDialog.show();
	}
	

	public void addBackColor(View view) {
		backColorDialog = new ColorPickerDialog(this, INITIAL_COLOR);
		backColorDialog.setAlphaSliderVisible(true);
		backColorDialog.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				backColor = color;
				setTextViewColor();
			}
		});
		backColorDialog.setOnCancelListener(colorPickerCancelListener);
		backColorDialog.show();
	}
}
