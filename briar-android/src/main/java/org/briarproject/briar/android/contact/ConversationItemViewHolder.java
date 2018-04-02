package org.briarproject.briar.android.contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.briarproject.bramble.api.contact.ContactManager;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.util.StringUtils;
import org.briarproject.briar.R;
import org.briarproject.briar.android.util.UiUtils;
import org.briarproject.briar.api.messaging.MessagingManager;
import org.briarproject.briar.android.view.SelectedMediaView;


import java.util.logging.Logger;

import javax.inject.Inject;

@UiThread
@NotNullByDefault
class ConversationItemViewHolder extends ViewHolder {
	protected Context ctx;
	protected final ViewGroup layout;

	private final TextView text;
	private final TextView time;
	//private final LinearLayout conversationMessageMediaContainer;
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
		ctx = v.getContext();

		layout = v.findViewById(R.id.layout);
		text = v.findViewById(R.id.text);
		time = v.findViewById(R.id.time);
		//conversationMessageMediaContainer = v.findViewById(R.id.conversationMessageMediaContainer);
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
							imageView.setImageBitmap(decodedMedia);

							mediaView.setImage(decodedMedia);
							//conversationMessageMediaContainer.addView(mediaView);
						} else {
							text.setText(StringUtils.trim(shims[i]));
						}
					}
				}
			} else {
				text.setText(StringUtils.trim(item.getBody().toString()));
			}
		}
		
		if(item.getClass() == ConversationMessageInItem.class) {
			try{
                //set the right icon
                if(messagingManager.isMessagePinned(item.getId())){
                    pinned.setActivated(true);
                    pinned.setImageResource(R.drawable.ic_bookmark_black_24dp);
                }
                else{
                    pinned.setActivated(false);
                    pinned.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                }
			} catch (DbException e){
                pinned.setActivated(false);
                pinned.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            }

			//Set image button listener
			pinned.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Remove from pinned messages if button is activated
					if(pinned.isActivated()){
						new Handler().post(new Runnable() {
							public void run() {
								try {
									messagingManager.setPinned(item.getId(), false);
								} catch (DbException e) {
									e.printStackTrace();
								}
							}
						});
                        pinned.setActivated(false);
                        pinned.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
					}
					//Add to pinned messages is button is deactivated
					else{
						new Handler().post(new Runnable() {
							public void run() {
								try {
									messagingManager.setPinned(item.getId(), true);
								} catch (DbException e) {
									e.printStackTrace();
								}
							}
						});
                        pinned.setActivated(true);
                        pinned.setImageResource(R.drawable.ic_bookmark_black_24dp);
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
