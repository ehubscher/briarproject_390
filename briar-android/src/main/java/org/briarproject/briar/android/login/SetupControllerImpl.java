package org.briarproject.briar.android.login;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.briarproject.bramble.api.crypto.CryptoComponent;
import org.briarproject.bramble.api.crypto.CryptoExecutor;
import org.briarproject.bramble.api.crypto.PasswordStrengthEstimator;
import org.briarproject.bramble.api.crypto.SecretKey;
import org.briarproject.bramble.api.db.DatabaseConfig;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.plugin.tcp.UniqueIDSingleton;
import org.briarproject.bramble.restClient.BServerServicesImpl;
import org.briarproject.bramble.restClient.ServerObj.PwdSingletonServer;
import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.briarproject.briar.android.controller.handler.ResultHandler;
import org.briarproject.briar.android.controller.handler.UiResultHandler;
import org.briarproject.briar.android.util.IOUniqueIdentifier;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.security.auth.callback.Callback;

@NotNullByDefault
public class SetupControllerImpl extends PasswordControllerImpl
		implements SetupController {

	@Nullable
	private String authorName, uniqueId, password;
	@Nullable
	private SetupActivity setupActivity;
	private volatile boolean creationCompleted = false;

	@Inject
	SetupControllerImpl(SharedPreferences briarPrefs,
						DatabaseConfig databaseConfig,
						@CryptoExecutor Executor cryptoExecutor, CryptoComponent crypto,
						PasswordStrengthEstimator strengthEstimator) {
		super(briarPrefs, databaseConfig, cryptoExecutor, crypto,
				strengthEstimator);
	}

	@Override
	public void setSetupActivity(SetupActivity setupActivity) {
		this.setupActivity = setupActivity;
	}

	@Override
	public boolean needToShowDozeFragment() {
		if (setupActivity == null) throw new IllegalStateException();
		return DozeView.needsToBeShown(setupActivity) ||
				HuaweiView.needsToBeShown(setupActivity);
	}

	@Override
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
		this.uniqueId = authorName;

		//put in the share preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.setupActivity.getApplicationContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("uniqueId", authorName);
		editor.apply();

		if (setupActivity == null) throw new IllegalStateException();
		setupActivity.showPasswordFragment();
	}

	@Override
	public String setUniqueId() {
		IOUniqueIdentifier ioUniqueIdentifier = new IOUniqueIdentifier();
		UniqueIDSingleton.setUniqueID(this.uniqueId);
		this.uniqueId = ioUniqueIdentifier.getUniqueID();


		return this.uniqueId;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void showDozeOrCreateAccount() {
		if (setupActivity == null) throw new IllegalStateException();
		if (needToShowDozeFragment()) {
			setupActivity.showDozeFragment();
		} else {
			createAccount();
		}
	}

	@Override
	public void createAccount() {
	    // Let's call our server before switching to real app
        PwdSingletonServer.setPassword(password);
        bootstrapCreateUserOnServer();
		UiResultHandler<Void> resultHandler =
				new UiResultHandler<Void>(setupActivity) {
					@Override
					public void onResultUi(Void result) {
						if (setupActivity == null)
							throw new IllegalStateException();
						setupActivity.showApp();
					}
				};
		createAccount(resultHandler);
	}

	@Override
	public void createAccount(ResultHandler<Void> resultHandler) {
		if (authorName == null || password == null)
			throw new IllegalStateException();


		cryptoExecutor.execute(() -> {
			databaseConfig.setLocalAuthorName(authorName);
			databaseConfig.setLocalUniqueId(uniqueId);
			SecretKey key = crypto.generateSecretKey();
			databaseConfig.setEncryptionKey(key);
			String hex = encryptDatabaseKey(key, password);
			storeEncryptedDatabaseKey(hex);
			resultHandler.onResult(null);
		});

	}

	private void bootstrapCreateUserOnServer() {
		boolean successCreation = false;
		try {
			new CallServerAsync().execute();
			if(!creationCompleted){
				// Try to call again ???
				new CallServerAsync().execute();
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		if (!successCreation) {
			Log.d("INFO", "NO SUCCESS CREATING USER TO SERVER");
		}
	}

	/**
	 * This class is implementing an Async task as recommended for Android
	 * It is made to make sure to separate server call from main UI Thread
	 */
	class CallServerAsync extends AsyncTask<Void, Integer, String> {

		boolean resultFromCreateAccount;

		@Override
		protected String doInBackground(Void... voids) {
			BServerServicesImpl services = new BServerServicesImpl();
			SavedUser placeHolderUser = new SavedUser(authorName, "123.123.123.123", 1234, 1, 99);
			resultFromCreateAccount = services.createNewUser(placeHolderUser, PwdSingletonServer.getPassword());
			return null;
		}

		protected void onPostExecute(String result) {
			creationCompleted = resultFromCreateAccount;
		}
	}
}


