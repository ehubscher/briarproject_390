package org.briarproject.briar.android.contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.util.StringUtils;
import org.briarproject.briar.R;
import org.briarproject.briar.android.util.UiUtils;
import org.briarproject.briar.android.view.SelectedMediaView;

@UiThread
@NotNullByDefault
class ConversationItemViewHolder extends ViewHolder {
	protected Context ctx;
	protected final ViewGroup layout;

	private final TextView text;
	private final TextView time;
	private final LinearLayout mediaMessageContainer;
	private final ImageView imageView;

	ConversationItemViewHolder(View v) {
		super(v);
		ctx = v.getContext();

		layout = v.findViewById(R.id.layout);
		text = v.findViewById(R.id.text);
		time = v.findViewById(R.id.time);
		mediaMessageContainer = v.findViewById(R.id.mediaMessageContainer); //mBackgroundTint.mTintList = NullPointerException
		imageView = v.findViewById(R.id.imageView);
	}

	@CallSuper
	void bind(ConversationItem item) {
		if (item.getBody() == null) {
			text.setText("\u2026");
		} else {
			if (item.getBody().contains("%shim%")) {
				String[] shims = item.getBody().split("%shim%");
				String encodedMedia = "";

				if (shims.length > 0) {
					for (int i = 0; i < shims.length; i++) {
						if (shims[i].startsWith("ImageTag:")) {
							SelectedMediaView mediaView = new SelectedMediaView(this.ctx);
							encodedMedia = shims[i].substring(9);
							byte[] mediaBytes = android.util.Base64.decode(encodedMedia, android.util.Base64.DEFAULT);

							Bitmap decodedMedia = BitmapFactory.decodeByteArray(mediaBytes, 0, mediaBytes.length);
							mediaView.setImage(decodedMedia);

							imageView.setImageBitmap(decodedMedia);
							//mediaMessageContainer.addView(mediaView);
						} else {
							text.setText(StringUtils.trim(shims[i]));
						}
					}
				}
			} else {
				text.setText(StringUtils.trim(item.getBody().toString()));
			}
		}

		long timestamp = item.getTime();
		time.setText(UiUtils.formatDate(time.getContext(), timestamp));
	}

	public TextView getText() {
		return text;
	}
}
