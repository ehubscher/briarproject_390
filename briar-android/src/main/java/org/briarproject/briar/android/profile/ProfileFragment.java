package org.briarproject.briar.android.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.briarproject.bramble.api.contact.event.ContactAvatarChangedEvent;
import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.avatar.AvatarActivity;
import org.briarproject.briar.android.avatar.AvatarActivityFragment;
import org.briarproject.briar.android.fragment.BaseFragment;


public class ProfileFragment extends BaseFragment {


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



		return rootView;
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

