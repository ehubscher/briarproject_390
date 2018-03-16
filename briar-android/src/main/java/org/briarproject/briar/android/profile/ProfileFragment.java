package org.briarproject.briar.android.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.avatar.AvatarActivityFragment;
import org.briarproject.briar.android.fragment.BaseFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class ProfileFragment extends BaseFragment {

	ImageView avatarImage;
	int imageNb;
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

	    //Set image
		avatarImage = rootView.findViewById(R.id.image_avatar_profile);

	    /*Retrieving stored avatar*/
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
		int avatarId= settings.getInt("pref_avatar",0);
		if(avatarId != 0) {
			avatarImage.setImageResource(mThumbIds[avatarId - 1]);
		}
		else{
			avatarImage.setImageResource(R.drawable.avatar);
			//TODO: display user's identicon
			//avatarImage.setImageResource(new IdenticonDrawable(author.getId().getBytes())));
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

		/*Retrieving stored status*/
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
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("current_status", status_num);
		editor.commit();

		//TODO: broadcast event
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

		//Store avatar number in preferences
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("pref_avatar", avatarNumber);
		editor.commit();

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

}

