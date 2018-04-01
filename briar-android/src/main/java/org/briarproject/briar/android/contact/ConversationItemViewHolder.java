package org.briarproject.briar.android.contact;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.briarproject.bramble.api.contact.ContactManager;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.util.StringUtils;
import org.briarproject.briar.R;
import org.briarproject.briar.android.util.UiUtils;
import org.briarproject.briar.api.messaging.MessagingManager;

import java.util.logging.Logger;

import javax.inject.Inject;

@UiThread
@NotNullByDefault
class ConversationItemViewHolder extends ViewHolder {
	protected final ViewGroup layout;

	private final TextView text;
	private final TextView time;
	private final ImageView imageView;
	private final ImageButton pinned;

	// Fields that are accessed from background threads must be volatile
	@Inject
	volatile ContactManager contactManager;

	@Inject
	volatile MessagingManager messagingManager;

    private static final Logger LOG = Logger.getLogger(ConversationActivity.class.getName());

	ConversationItemViewHolder(View v) {
		super(v);
		layout = v.findViewById(R.id.layout);
		text = v.findViewById(R.id.text);
		time = v.findViewById(R.id.time);
		imageView = v.findViewById(R.id.imageView);
		pinned = v.findViewById(R.id.pin_message_bookmark);
	}

	public void setMessagingManager(MessagingManager messagingManager) {
		this.messagingManager = messagingManager;
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
		
		if(item.getClass() == ConversationMessageInItem.class) {
			//set the right icon
			if(item.isPinned()){
				pinned.setActivated(true);
				pinned.setImageResource(R.drawable.ic_bookmark_black_24dp);
			}
			else{
				pinned.setActivated(false);
				pinned.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
			}

			//Set image button listener
			pinned.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Remove from pinned messages if button is activated
					if(pinned.isActivated()){
						pinned.setActivated(false);
						pinned.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
						item.setPinned(false);

						new Handler().post(new Runnable() {
							public void run() {
								try {
									messagingManager.setPinned(item.getId(), false);
								} catch (DbException e) {
									e.printStackTrace();
								}
							}
						});

					}//Add to pinned messages is button is deactivated
					else{
						pinned.setActivated(true);
						pinned.setImageResource(R.drawable.ic_bookmark_black_24dp);
						item.setPinned(true);
						new Handler().post(new Runnable() {
							public void run() {
								try {
									messagingManager.setPinned(item.getId(), true);
								} catch (DbException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			});
		}
		long timestamp = item.getTime();
		time.setText(UiUtils.formatDate(time.getContext(), timestamp));
	}

	public TextView getText() {
		return text;
	}
}
