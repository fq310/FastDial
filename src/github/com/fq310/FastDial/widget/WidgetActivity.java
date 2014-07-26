package github.com.fq310.FastDial.widget;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerDialog.OnColorChangedListener;
import github.com.fq310.FastDial.git.R;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

public class WidgetActivity extends Activity {
	
	private static final int INITIAL_COLOR = 0xFFFF4444;
	private static final int DEFAULT_BACK_COLOR = 0xFF4444;
	private static final int DEFAULT_FORE_COLOR = 0x99CC00;
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
	}

	private void setTextViewColor() {
		findViewById(R.id.textView_ForeColor).setBackgroundColor(foreColor);
		findViewById(R.id.textView_BackColor).setBackgroundColor(backColor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.widget, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
	
	public void onCancel(View v) {
		finish();
	}
	
	public void onOK(View v) {
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
		views.setTextViewText(R.id.textView_widget, getName());
		views.setTextColor(R.id.textView_widget, foreColor);
		views.setInt(R.id.textView_widget, "setBackgroundColor", backColor);
		views.setFloat(R.id.textView_widget, "setTextSize", 70);
		appWidgetManager.updateAppWidget(mAppWidgetId, views);
		
		finish();
	}

	private String getName() {
		EditText text = (EditText) this.findViewById(R.id.editText_name);
		return text.getText().toString().trim();
	}
	
	public void addForeColor(View view) {
		ColorPickerDialog d = new ColorPickerDialog(this, INITIAL_COLOR);
		d.setAlphaSliderVisible(true);
		d.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				foreColor = color;
				setTextViewColor();
			}
		});
		d.setOnCancelListener(colorPickerCancelListener);
		d.show();
	}
	

	public void addBackColor(View view) {
		ColorPickerDialog d = new ColorPickerDialog(this, INITIAL_COLOR);
		d.setAlphaSliderVisible(true);
		d.setOnColorChangedListener(new OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				backColor = color;
				setTextViewColor();
			}
		});
		d.setOnCancelListener(colorPickerCancelListener);
		d.show();
	}
}
