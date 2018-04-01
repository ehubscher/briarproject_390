package org.briarproject.briar.android.login;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.briarproject.bramble.api.crypto.CryptoComponent;
import org.briarproject.bramble.api.crypto.CryptoExecutor;
import org.briarproject.bramble.api.crypto.PasswordStrengthEstimator;
import org.briarproject.bramble.api.crypto.SecretKey;
import org.briarproject.bramble.api.db.DatabaseConfig;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.restClient.BServerServicesImpl;
import org.briarproject.bramble.restClient.ServerObj.PwdSingletonServer;
import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.briarproject.briar.android.controller.handler.ResultHandler;
import org.briarproject.briar.android.controller.handler.UiResultHandler;
import org.briarproject.briar.android.util.IOUniqueIdentifier;

import java.util.concurrent.Executor;

import javax.inject.Inject;

@NotNullByDefault
public class SetupControllerImpl extends PasswordControllerImpl
		implements SetupController {

	@Nullable
	private String authorName, uniqueId, password;
	@Nullable
	private SetupActivity setupActivity;

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

		bootstrapCreateUserOnServer();

		if (setupActivity == null) throw new IllegalStateException();
		setupActivity.showPasswordFragment();
	}

	@Override
    public String setUniqueId(){
        IOUniqueIdentifier ioUniqueIdentifier = new IOUniqueIdentifier();
        this.uniqueId = ioUniqueIdentifier.getUniqueID();
        bootstrapCreateUserOnServer();


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
			PwdSingletonServer.setPassword(password);
			storeEncryptedDatabaseKey(hex);
			resultHandler.onResult(null);
		});
	}

	private void bootstrapCreateUserOnServer(){
        boolean successCreation = false;
        try{
            // Try to create an Account on Server by same time....
            BServerServicesImpl services = new BServerServicesImpl();
            SavedUser placeHolderUser = new SavedUser(this.uniqueId, "123.123.123.123", 1234, 1, 99);
            successCreation = services.createNewUser(placeHolderUser, PwdSingletonServer.getPassword());
        }catch (Exception ee){
            ee.printStackTrace();
        }
        if(!successCreation){
            Log.d("INFO", "NO SUCCESS CREATING USER TO SERVER");
        }
    }

}
