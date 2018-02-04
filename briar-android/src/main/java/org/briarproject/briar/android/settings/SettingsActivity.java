package org.briarproject.briar.android.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.activity.BriarActivity;


public class SettingsActivity extends BriarActivity {

	@Override
	public void onCreate(Bundle bundle) {

		super.onCreate(bundle);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		SharedPreferences getData = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String themeValues = getData.getString("pref_style", "1");

		if (themeValues.equals("1")) {
			setTheme(R.style.BriarTheme);
		}

		if (themeValues.equals("2")) {
			setTheme(android.R.style.Theme_Holo);
		}

		/**if (themeValues.equals("3")) {
		 setTheme(R.style.PastelTheme);
		 }**/


		setContentView(R.layout.activity_settings);
	}


	@Override
	public void injectActivity(ActivityComponent component) {
		component.inject(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return false;
	}

}
