package org.briarproject.briar.android.profile;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
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
	    GridView allIcons = rootView.findViewById(R.id.grid_view);
	    allIcons.setAdapter(new ImageAdapter(rootView.getContext()));

	    allIcons.setOnItemClickListener((parent, v, position, id) -> {
		    // empty
	    });

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

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mThumbIds.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				// if it's not recycled, initialize some attributes
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}

		// references to our images
		private Integer[] mThumbIds = {
				R.drawable.ic_android_black_24dp,
				R.drawable.ic_sentiment_very_satisfied_black_24dp,
				R.drawable.ic_mood_black_24dp,
		};
	}
}

