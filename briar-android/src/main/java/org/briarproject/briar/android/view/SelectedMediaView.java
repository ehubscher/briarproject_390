package org.briarproject.briar.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import org.briarproject.briar.R;

import javax.annotation.Nullable;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SelectedMediaView extends android.support.v7.widget.AppCompatImageView {
    public SelectedMediaView(Context context) {
        this(context, null);
    }

    public SelectedMediaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectedMediaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        int hi = R.layout.selected_media_view;
        //inflater.inflate(R.layout.selected_media_view,);
    }
}
