package org.briarproject.briar.android.avatar;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.activity.BriarActivity;

public class AvatarActivity extends BriarActivity{

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		setContentView(R.layout.activity_avatar);
	}

	@Override
	public void onStart(){
		super.onStart();
	}

	@Override
	public void injectActivity(ActivityComponent component) {

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
