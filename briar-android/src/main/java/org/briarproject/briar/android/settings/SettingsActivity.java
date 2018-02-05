package org.briarproject.briar.android.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.activity.BriarActivity;




public class SettingsActivity extends BriarActivity {

	private int mCurrentTheme;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		this.mCurrentTheme = this.getThemeId(this);
		this.setTheme(this.mCurrentTheme);

		setContentView(R.layout.activity_settings);
	}

	@Override
	public void onStart(){
		super.onStart();
		int newTheme = this.getThemeId(this);
		if(this.mCurrentTheme != newTheme) {
			this.finish();
			this.startActivity(new Intent(this, this.getClass()));
		}

	}

	public int getThemeId(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String theme = settings.getString(context.getResources().getString(R.string.pref_theme), null);

		if (theme == null || theme.equals("1")) {
			return android.R.style.Theme_Holo;
		} else if (theme.equals("2")) {
			return android.R.style.Theme_Holo;
		}

		// default
		return R.style.BriarTheme;
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
