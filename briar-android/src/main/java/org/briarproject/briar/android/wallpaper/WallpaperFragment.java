package org.briarproject.briar.android.wallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.briarproject.briar.R;


public class WallpaperFragment extends Fragment {

    private Integer[] wallpapers = {
            R.drawable.bubbles,
            R.drawable.flower,
            R.drawable.hellokitty,
            R.drawable.lake,
            R.drawable.sunset,
            R.drawable.nebula,
            R.drawable.abstractimage,
            R.drawable.animalcrossing,
            R.drawable.burst,
            R.drawable.cloud,
            R.drawable.fireflies,
            R.drawable.orangepattern,
            R.drawable.park,
            R.drawable.planet,
            R.drawable.water,
            R.drawable.watercolor,
            R.drawable.mario,
            R.drawable.zelda,
            R.drawable.goldengate,
            R.drawable.mountain,
            R.drawable.space,
            R.drawable.vcolors,
            R.drawable.kirby,
            R.drawable.shanghai


    };

    public WallpaperFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wallpaper, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridview_wallpaper);
        gridView.setAdapter(new WallpaperFragment.ImageAdapter(view.getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id)
            {
                Toast.makeText(getActivity(),
                        "Wallpaper saved",
                        Toast.LENGTH_SHORT).show();
                saveWallpaper(position+1);
            }
        });
        return view;
    }

    public void saveWallpaper(int wallpaperId){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("wallpaper", wallpaperId);
        editor.apply();
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter(Context c) {
            context = c;
        }

        //number of images stored in the array
        public int getCount() {
            return wallpapers.length;
        }

        //Id of an item inside the gridview
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //returns the ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(190, 210));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(5, 5, 5, 5);
            }
            else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(wallpapers[position]);
            return imageView;
        }
    }
}
