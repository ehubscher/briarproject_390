package org.briarproject.briar.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;

import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.settings.Settings;
import org.briarproject.bramble.api.settings.SettingsManager;
import org.briarproject.briar.R;
import org.briarproject.briar.android.AndroidComponent;
import org.briarproject.briar.android.BriarApplication;
import org.briarproject.briar.android.DestroyableContext;
import org.briarproject.briar.android.controller.ActivityLifecycleController;
import org.briarproject.briar.android.forum.ForumModule;
import org.briarproject.briar.android.fragment.BaseFragment;
import org.briarproject.briar.android.fragment.ScreenFilterDialogFragment;
import org.briarproject.briar.android.settings.SettingsActivity;
import org.briarproject.briar.android.settings.SettingsFragment;
import org.briarproject.briar.android.widget.TapSafeFrameLayout;
import org.briarproject.briar.android.widget.TapSafeFrameLayout.OnTapFilteredListener;
import org.briarproject.briar.api.android.ScreenFilterMonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Inject;



import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.FLAG_SECURE;
import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;
import static java.util.logging.Level.WARNING;
import static org.briarproject.briar.android.TestingConstants.PREVENT_SCREENSHOTS;


public abstract class BaseActivity extends AppCompatActivity
		implements DestroyableContext, OnTapFilteredListener {

	/* ----FOR THEME----*/
	private static final Logger LOG2 =
			Logger.getLogger(SettingsFragment.class.getName());
	private SettingsActivity listener2;
	@Inject
	volatile SettingsManager settingsManager;


	@Inject
	protected ScreenFilterMonitor screenFilterMonitor;

	protected ActivityComponent activityComponent;

	private final List<ActivityLifecycleController> lifecycleControllers =
			new ArrayList<>();
	private boolean destroyed = false;
	private ScreenFilterDialogFragment dialogFrag;
	public abstract void injectActivity(ActivityComponent component);
	//private int themePref;
	private int mCurrentTheme;


	public void addLifecycleController(ActivityLifecycleController alc) {
		lifecycleControllers.add(alc);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {

		/*
		Settings s = new Settings();
		Settings themeSettings = settingsManager.getSettings(THEME_NAMESPACE);
		String themeSetting = themeSettings.getInt();*/

		/*if (theme.equals("2")) {
			setTheme(android.R.style.Theme_Holo);
		} else if (theme.equals("3")) {
			 setTheme(android.R.style.Theme_Holo_Light);
		}*/

		super.onCreate(savedInstanceState);


		/* THEME */
		this.mCurrentTheme = getThemeId(this);

		if (mCurrentTheme == 2) {
			setTheme(android.R.style.Theme_Holo);
		} else if (mCurrentTheme == 3) {
			setTheme(android.R.style.Theme_Holo_Light);
		} else {
			setTheme(R.style.BriarTheme);
		}

		setTheme(android.R.style.Theme_Holo);

		/*this.mCurrentTheme = this.getThemeId(this);
		this.setTheme(this.mCurrentTheme);*/

		if (PREVENT_SCREENSHOTS) getWindow().addFlags(FLAG_SECURE);

		AndroidComponent applicationComponent =
				((BriarApplication) getApplication()).getApplicationComponent();

		activityComponent = DaggerActivityComponent.builder()
				.androidComponent(applicationComponent)
				.activityModule(getActivityModule())
				.forumModule(getForumModule())
				.build();

		injectActivity(activityComponent);

		for (ActivityLifecycleController alc : lifecycleControllers) {
			alc.onActivityCreate(this);
		}


	}

	public int getThemeId(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String theme = settings.getString(context.getResources().getString(R.string.pref_theme),"");

		if (theme.equals("theme_dark")) {
			return R.style.DarkTheme;
		} else if (theme.equals("theme_pastel")) {
			return R.style.PastelTheme;
		}

		// default
		return R.style.BriarTheme;
	}

	public ActivityComponent getActivityComponent() {
		return activityComponent;
	}

	// This exists to make test overrides easier
	protected ActivityModule getActivityModule() {
		return new ActivityModule(this);
	}

	protected ForumModule getForumModule() {
		return new ForumModule();
	}

	@Override
	protected void onStart() {
		super.onStart();
		int newTheme = this.getThemeId(this);
		if(this.mCurrentTheme != newTheme) {
			this.finish();
			this.startActivity(new Intent(this, this.getClass()));
		}
		for (ActivityLifecycleController alc : lifecycleControllers) {
			alc.onActivityStart();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		for (ActivityLifecycleController alc : lifecycleControllers) {
			alc.onActivityStop();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (dialogFrag != null) {
			dialogFrag.dismiss();
			dialogFrag = null;
		}
	}

	protected void showInitialFragment(BaseFragment f) {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentContainer, f, f.getUniqueTag())
				.commit();
	}

	public void showNextFragment(BaseFragment f) {
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.step_next_in,
						R.anim.step_previous_out, R.anim.step_previous_in,
						R.anim.step_next_out)
				.replace(R.id.fragmentContainer, f, f.getUniqueTag())
				.addToBackStack(f.getUniqueTag())
				.commit();
	}

	private void showScreenFilterWarning() {
		if (dialogFrag != null && dialogFrag.isVisible()) return;
		Set<String> apps = screenFilterMonitor.getApps();
		if (apps.isEmpty()) return;
		dialogFrag =
				ScreenFilterDialogFragment.newInstance(new ArrayList<>(apps));
		dialogFrag.setCancelable(false);
		// Show dialog unless onSaveInstanceState() has been called, see #1112
		FragmentManager fm = getSupportFragmentManager();
		if (!fm.isStateSaved()) dialogFrag.show(fm, dialogFrag.getTag());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyed = true;
		for (ActivityLifecycleController alc : lifecycleControllers) {
			alc.onActivityDestroy();
		}
	}

	@Override
	public void runOnUiThreadUnlessDestroyed(Runnable r) {
		runOnUiThread(() -> {
			if (!destroyed && !isFinishing()) r.run();
		});
	}

	public void showSoftKeyboard(View view) {
		Object o = getSystemService(INPUT_METHOD_SERVICE);
		((InputMethodManager) o).showSoftInput(view, SHOW_IMPLICIT);
	}

	public void hideSoftKeyboard(View view) {
		IBinder token = view.getWindowToken();
		Object o = getSystemService(INPUT_METHOD_SERVICE);
		((InputMethodManager) o).hideSoftInputFromWindow(token, 0);
	}

	@UiThread
	public void handleDbException(DbException e) {
		supportFinishAfterTransition();
	}

	/*
	 * Wraps the given view in a wrapper that notifies this activity when an
	 * obscured touch has been filtered, and returns the wrapper.
	 */
	private View makeTapSafeWrapper(View v) {
		TapSafeFrameLayout wrapper = new TapSafeFrameLayout(this);
		wrapper.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
		wrapper.setOnTapFilteredListener(this);
		wrapper.addView(v);
		return wrapper;
	}

	/*
	 * Finds the AppCompat toolbar, if any, and configures it to filter
	 * obscured touches. If a custom toolbar is used, it will be part of the
	 * content view and thus protected by the wrapper. But the default toolbar
	 * is outside the wrapper.
	 */
	private void protectToolbar() {
		View decorView = getWindow().getDecorView();
		if (decorView instanceof ViewGroup) {
			Toolbar toolbar = findToolbar((ViewGroup) decorView);
			if (toolbar != null) toolbar.setFilterTouchesWhenObscured(true);
		}
	}

	@Nullable
	private Toolbar findToolbar(ViewGroup vg) {
		for (int i = 0, len = vg.getChildCount(); i < len; i++) {
			View child = vg.getChildAt(i);
			if (child instanceof Toolbar) return (Toolbar) child;
			if (child instanceof ViewGroup) {
				Toolbar toolbar = findToolbar((ViewGroup) child);
				if (toolbar != null) return toolbar;
			}
		}
		return null;
	}

	@Override
	public void setContentView(@LayoutRes int layoutRes) {
		setContentView(getLayoutInflater().inflate(layoutRes, null));
	}

	@Override
	public void setContentView(View v) {
		super.setContentView(makeTapSafeWrapper(v));
		protectToolbar();
	}

	@Override
	public void setContentView(View v, LayoutParams layoutParams) {
		super.setContentView(makeTapSafeWrapper(v), layoutParams);
		protectToolbar();
	}

	@Override
	public void addContentView(View v, LayoutParams layoutParams) {
		super.addContentView(makeTapSafeWrapper(v), layoutParams);
		protectToolbar();
	}

	@Override
	public void onTapFiltered() {
		showScreenFilterWarning();
	}


	/*public int setTheme() {

		//listener 2 needs to be instantiated. consider renaming preftheme and listener2
		listener2.runOnDbThread(() -> {
			try {
				final Settings themeSettings = settingsManager.getSettings("theme");

				themePref = themeSettings.getInt("pref_theme", 1);

			} catch (DbException e) {
				if (LOG2.isLoggable(WARNING)) LOG2.log(WARNING, e.toString(), e);
			}
		});
		return this.themePref;

	}*/
}
