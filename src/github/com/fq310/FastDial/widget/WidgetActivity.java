package github.com.fq310.FastDial.widget;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerDialog.OnColorChangedListener;
import github.com.fq310.FastDial.git.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class WidgetActivity extends Activity {
	
	private static final int INITIAL_COLOR = 0xFFFF4444;
	private static final int DEFAULT_BACK_COLOR = 0xFF141414;
	private static final int DEFAULT_FORE_COLOR = 0xFFFF3030;
	private int foreColor = DEFAULT_FORE_COLOR;
	private int backColor = DEFAULT_BACK_COLOR;
	private int mAppWidgetId;
	private int textSize = 40;
	private int minSize = 40;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget);
		setTextViewColor();
		configID();
		initialPreivewTextSize();
		addNameListener();
		addSeekbarListener();
	}

	private void initialPreivewTextSize() {
		getPreviewText().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		getPreviewText().setTextSize(textSize);
		getTextSizeText().setText(String.valueOf(textSize));
	}

	private TextView getTextSizeText() {
		return (TextView)findViewById(R.id.textView_textSize);
	}

	private TextView getPreviewText() {
		return (TextView)findViewById(R.id.textView_preview);
	}

	private void addSeekbarListener() {
		final SeekBar setSize = (SeekBar)findViewById(R.id.setSizeBar);
		final TextView textSizeText = getTextSizeText();
		final TextView previewText = getPreviewText();
		setSize.setMax(250);
		setSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int arg1, boolean arg2) {
				int value = seekBar.getProgress();
				textSize = value + minSize;
				previewText.setTextSize(textSize);
				textSizeText.setText(String.valueOf(textSize));
			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}});
	}

	private void addNameListener() {
		EditText nameText = (EditText)findViewById(R.id.editText_name);
		final TextView previewText = getPreviewText();
		nameText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable nameText) {
				CharSequence name = nameText.subSequence(0, nameText.length());
				previewText.setText(name);
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}});
	}

	private void configID() {
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
		if (isEmptyInput()) {
			showWarningDialog();
			return;
		}
		destroyColorPickerDialog();
		initialWidget();
		
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();
	}
	
	private void showWarningDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(R.string.warning_message);
		builder.setTitle(R.string.warning_title);
		builder.setPositiveButton(R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	private boolean isEmptyInput() {
		if (getName().trim().length() == 0 ||
			getNumber().trim().length() == 0)
			return true;
		return false;
	}

	private void initialWidget() {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
		views.setTextViewText(R.id.textView_widget, getName());
		views.setTextColor(R.id.textView_widget, foreColor);
		views.setInt(R.id.textView_widget, "setBackgroundColor", backColor);
		views.setFloat(R.id.textView_widget, "setTextSize", textSize);
		
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
