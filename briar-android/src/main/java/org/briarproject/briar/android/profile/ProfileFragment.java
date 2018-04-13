package org.briarproject.briar.android.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.briarproject.bramble.plugin.tcp.UniqueIDSingleton;
import org.briarproject.bramble.restClient.BServerServicesImpl;
import org.briarproject.bramble.restClient.ServerObj.PreferenceUser;
import org.briarproject.bramble.restClient.ServerObj.PwdSingletonServer;
import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.contact.ConversationActivity;
import org.briarproject.briar.android.fragment.BaseFragment;

import java.util.logging.Logger;

import static java.util.logging.Level.WARNING;


public class ProfileFragment extends BaseFragment {

    UniqueIDSingleton uniqueIDSingleton;
    private static volatile SavedUser currentPhoneHolder;
	private volatile boolean updateSuccess = false;
	private volatile String username;
    private static final Logger LOG =
            Logger.getLogger(ConversationActivity.class.getName());

	TextView uniqueIdTag;
    ImageView avatarImage;
	// references to our images
	private Integer[] mThumbIds = {
			R.drawable.pig,
			R.drawable.panda,
			R.drawable.dog,
			R.drawable.cat,
			R.drawable.bunny,
			R.drawable.monkey,
			R.drawable.frog,
			R.drawable.penguin,
			R.drawable.robot
	};

	Button buttonStatus;
	Button buttonAvatar;

	@Override
	public String getUniqueTag() {
		return null;
	}

	@Override
	public void injectFragment(ActivityComponent component) {
	}

	@Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
	    getActivity().setTitle(R.string.title_activity_profile);
		avatarImage = rootView.findViewById(R.id.image_avatar_profile);

	    //initialize a shared preference
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());

        //Retrieving stored avatar
		int avatarId= settings.getInt("pref_avatar",99);
		if(avatarId != 99) {
			avatarImage.setImageResource(mThumbIds[avatarId - 1]);
		}
		else{
			avatarImage.setImageResource(R.drawable.avatar);
			//TODO: display user's identicon
			//avatarImage.setImageResource(new IdenticonDrawable(author.getId().getBytes())));
		}

        //Set the text field to show the localUserID
        uniqueIdTag = rootView.findViewById(R.id.localUniqueId);
		String uniqueId = settings.getString("uniqueId", "1233345");
		// Make sure to setup Username correctly
		if(!uniqueId.isEmpty() & !uniqueId.equals("1233345")){
            uniqueIdTag.setText(uniqueId);
            username = uniqueId;
			if(UniqueIDSingleton.getUniqueID() == null || UniqueIDSingleton.getUniqueID().isEmpty()){
				UniqueIDSingleton.setUniqueID(uniqueId);
			}
        }

	    // Let's obtain SavedUser data from server...
	    try{
		    new CallServerAsyncObtainUser().execute();
	    }catch (Exception ee){
			LOG.info("BRIAR PROFILE : PROBLEM WHILE CALLING SERVER ");
	    }

		//Avatar button
	    buttonAvatar = (Button) rootView.findViewById(R.id.choose_avatar_button);
	    buttonAvatar.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    avatarMenu();
		    }
	    });

	    //Status button
		buttonStatus = (Button) rootView.findViewById(R.id.set_status_button);
		buttonStatus.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				statusMenu();
			}
		});

		//Retrieving stored status
		int statusID=settings.getInt("current_status",1);
		Drawable status;
		if(statusID==2){
			status = getResources().getDrawable(R.drawable.contact_busy);

		}else if(statusID==3){
			status = getResources().getDrawable(R.drawable.contact_disconnected);
		}
		else{
			status = getResources().getDrawable(R.drawable.contact_connected);
		}
		buttonStatus.setCompoundDrawablesWithIntrinsicBounds(status,null,null,null);

		return rootView;
    }

    private void statusMenu(){
		PopupMenu popup = new PopupMenu(this.getContext(), buttonStatus);
		//Inflating the Popup using xml file
		popup.getMenuInflater()
				.inflate(R.menu.popup_status, popup.getMenu());

		//registering popup with OnMenuItemClickListener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				saveStatus((String)item.getTitle());
				return true;
			}
		});

		popup.show(); //showing popup menu
	}

	private void saveStatus(String item){
    	Drawable status;
    	int status_num;

		if(item.equals("Busy")){
    		status = getResources().getDrawable(R.drawable.contact_busy);
    		status_num=2;

		}else if(item.equals("Disconnected")){
			status = getResources().getDrawable(R.drawable.contact_disconnected);
			status_num=3;
		}
		else{
			status = getResources().getDrawable(R.drawable.contact_connected);
			status_num=1;
		}
		buttonStatus.setCompoundDrawablesWithIntrinsicBounds(status,null,null,null);

		//Store avatar number in preferences
		storeInPreferences("current_status",status_num);

		//Set the new statusId
        try{
            currentPhoneHolder.setStatusId(status_num);
            new CallServerAsyncUpdateUserSettings().execute();
			if(!updateSuccess){
				LOG.info(" FAIL TO UPDATE SETTINGS ");
			}
        }catch (Exception e){
			if (LOG.isLoggable(WARNING)) LOG.log(WARNING, e.toString(), e);
        }
	}


	public void avatarMenu() {
		PopupMenu popup = new PopupMenu(this.getContext(), buttonAvatar);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.avatar_menu, popup.getMenu());

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				saveAvatar((String)item.getTitle());
				return true;
			}
		});

		popup.show();
	}

	private void saveAvatar(String item){
		int avatarNumber = 0;
		switch (item) {
			case "Pig":
				avatarNumber = 1;
				break;
			case "Panda":
				avatarNumber = 2;
				break;
			case "Dog":
				avatarNumber = 3;
				break;
			case "Cat":
				avatarNumber = 4;
				break;
			case "Bunny":
				avatarNumber = 5;
				break;
			case "Monkey":
				avatarNumber = 6;
				break;
			case "Frog":
				avatarNumber = 7;
				break;
			case "Penguin":
				avatarNumber = 8;
				break;
			case "Robot":
				avatarNumber = 9;
				break;
		}
		avatarImage.setImageResource(mThumbIds[avatarNumber - 1]);
		//Store avatar number in preferences
		storeInPreferences("pref_avatar", avatarNumber);

		//Set the new avatarId
        try{
            currentPhoneHolder.setAvatarId(avatarNumber);
            new CallServerAsyncUpdateUserSettings().execute();
            if(!updateSuccess){
            	LOG.info(" FAIL TO UPDATE SETTINGS ");
			}
        }catch (Exception e){
            if (LOG.isLoggable(WARNING)) LOG.log(WARNING, e.toString(), e);
        }
	}

	private void storeInPreferences(String preference, int value){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(preference, value);
		editor.apply();
	}

    @Override
	public void onStart() {
    	super.onStart();
    }

    @Override
	public void onStop() {
    	super.onStop();
    }

    public static ProfileFragment newInstance() {
	    return new ProfileFragment();
    }

	/**
	 * This class is implementing an Async task as recommended for Android
	 * It is made to make sure to separate server call from main UI Thread
	 */
	class CallServerAsyncObtainUser extends AsyncTask<Void, Integer, String> {

		SavedUser resultFromObtainUser;

		@Override
		protected String doInBackground(Void... voids) {
			BServerServicesImpl services = new BServerServicesImpl();
			if(username != null && PwdSingletonServer.getPassword() != null){
				PreferenceUser preferenceUser = services.getUserPreferences(username);
				// Build fake SavedUser data
                SavedUser fakeSavedUser = new SavedUser(username, "123.123.123.123", 2222, preferenceUser.getStatusId(), preferenceUser.getAvatarId());
                resultFromObtainUser = fakeSavedUser;
			}else{
				LOG.info("BRIAR PROFILE : username OR pwd not saved");
			}

			return null;
		}

		protected void onPostExecute(String result) {
			currentPhoneHolder = resultFromObtainUser;
		}
	}
	/**
	 * This class is implementing an Async task as recommended for Android
	 * It is made to make sure to separate server call from main UI Thread
	 */
	class CallServerAsyncUpdateUserSettings extends AsyncTask<Void, Integer, String> {

		boolean resultFromUpdate;

		@Override
		protected String doInBackground(Void... voids) {
			BServerServicesImpl services = new BServerServicesImpl();
			if(currentPhoneHolder != null){
				resultFromUpdate =  services.updateUserSettingInfo(currentPhoneHolder);
			}

			return null;
		}

		protected void onPostExecute(String result) {
			updateSuccess = resultFromUpdate;
		}
	}




}

