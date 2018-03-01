package org.briarproject.briar.android.profile;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.activity.BriarActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends BriarActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_profile);

	    GridView gridView = findViewById(R.id.grid_view);
	    gridView.setAdapter(new ImageAdapter(this));

	    gridView.setOnItemClickListener((parent, v, position, id) -> Toast.makeText(getBaseContext(),
			    "pic" + (position + 1) + " selected",
			    Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onStart(){
        super.onStart();
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

	public class ImageAdapter extends BaseAdapter
	{
		private Context context;
		private Integer[] mThumbIds = {
				R.drawable.ic_android_black_24dp,
				R.drawable.ic_sentiment_very_satisfied_black_24dp,
				R.drawable.ic_mood_black_24dp,
		};

		public ImageAdapter(Context c)
		{
			context = c;
		}

		//---returns the number of images---
		public int getCount() {
			return mThumbIds.length;
		}

		//---returns the ID of an item---
		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		//---returns an ImageView view---
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				// if it's not recycled, initialize some attributes
				imageView = new ImageView(context);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}

	}

}

