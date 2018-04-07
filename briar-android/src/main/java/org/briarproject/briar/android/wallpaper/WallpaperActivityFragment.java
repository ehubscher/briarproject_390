package org.briarproject.briar.android.wallpaper;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.briarproject.briar.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class WallpaperActivityFragment extends Fragment {

    public WallpaperActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallpaper, container, false);
    }
}
