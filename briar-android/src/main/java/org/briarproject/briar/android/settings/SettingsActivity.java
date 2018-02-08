package org.briarproject.briar.android.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.ListPreference;
import android.view.MenuItem;
import android.widget.Toast;

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

		/*SharedPreferences sharedPrefs =
				PreferenceManager.getDefaultSharedPreferences(this);
		String themes = sharedPrefs.getString("pref_theme", "");
		switch (themes) {
			case "Default":
				setTheme(R.style.BriarTheme);
				break;
			case "Dark":
				setTheme(android.R.style.Theme_Black);
				break;
			case "Pastel":
				setTheme(android.R.style.Theme_Holo_Light);
		}*/


		this.mCurrentTheme = this.getThemeId(this);
		this.setTheme(this.mCurrentTheme);

		setContentView(R.layout.activity_settings);
	}

	@Override
	public void onBackPressed()
	{
		Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();

		//Close activity & restart it
		this.finish();
		final Intent intent = this.getIntent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		this.startActivity(intent);
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
		String theme = settings.getString(context.getResources().getString(R.string.pref_theme),"");

		if (theme.equals("2")) {
			return android.R.style.Theme_Holo;
		} else if (theme.equals("3")) {
			return android.R.style.Theme_Holo_Light;
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
			super.onBackPressed();
			return true;
		}
		return false;
	}

}
