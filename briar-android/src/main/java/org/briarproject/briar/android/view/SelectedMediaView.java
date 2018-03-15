package org.briarproject.briar.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.briarproject.briar.R;

import javax.annotation.Nullable;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SelectedMediaView extends FrameLayout {
    protected final ViewHolder ui;
    protected Uri uri;
    protected String mediaType;

    public SelectedMediaView(Context context) {
        this(context, null);
    }

    public SelectedMediaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectedMediaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mediaView = inflater.inflate(R.layout.selected_media_view, this, true);
        ui = new ViewHolder();
    }

    public Uri getUri() {
        return this.uri;
    }

    public String getType() {
        return this.mediaType;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setImage(Bitmap bitmap) {
        ImageView image = new ImageView(this.getContext());
        image.setImageBitmap(bitmap);
        ui.selectedMedia.addView(image);
    }

    protected class ViewHolder {
        final FrameLayout selectedMedia;
        final ImageButton deleteButton;

        ViewHolder() {
            selectedMedia = findViewById(R.id.selectedMedia);

            deleteButton = findViewById(R.id.selectedMediaDeleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewGroup container = ((ViewGroup)v.getParent().getParent());
                    container.removeView((ViewGroup)v.getParent());
                }
            });
        }
    }
}
