package org.briarproject.briar.android.contact;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.briarproject.bramble.api.contact.ContactManager;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.db.NoSuchContactException;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.util.StringUtils;
import org.briarproject.briar.R;
import org.briarproject.briar.android.util.UiUtils;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import static java.util.logging.Level.WARNING;

@UiThread
@NotNullByDefault
class ConversationItemViewHolder extends ViewHolder {

	protected final ViewGroup layout;

	private final TextView text;
	private final TextView time;
	private final ImageView imageView;

	// Fields that are accessed from background threads must be volatile
	@Inject
	volatile ContactManager contactManager;

    private static final Logger LOG = Logger.getLogger(ConversationActivity.class.getName());

	ConversationItemViewHolder(View v) {
		super(v);
		layout = v.findViewById(R.id.layout);
		text = v.findViewById(R.id.text);
		time = v.findViewById(R.id.time);
		imageView = v.findViewById(R.id.imageView);
	}

	@CallSuper
	void bind(ConversationItem item) {
		if (item.getBody() == null) {
			text.setText("\u2026");
		}else {
			//check if the body string is a base64 encode image
			if(item.getBody().startsWith("ImageTag:")){

			    String encodedString = item.getBody().substring(9);

				byte[] imageBytes = android.util.Base64.decode(encodedString, android.util.Base64.DEFAULT);
				Bitmap decodeImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                text.setText("Image:");
				imageView.setImageBitmap(decodeImage);

			}
			//Displays the normal text if the string isn't a base64 image
			else{
				text.setText(StringUtils.trim(item.getBody()));
			}
		}

		long timestamp = item.getTime();
		time.setText(UiUtils.formatDate(time.getContext(), timestamp));
	}

	public TextView getText() {
		return text;
	}
}
