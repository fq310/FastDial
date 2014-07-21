package github.com.fq310.FastDial.widget;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import github.com.fq310.FastDial.git.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class WidgetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget);
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
	
	public void addForeColor(View view) {
		ColorPickerDialog d = new ColorPickerDialog(this, 0x000000);
		d.setAlphaSliderVisible(true);
		d.show();
	}
	

	public void addBackColor(View view) {
	}
}
