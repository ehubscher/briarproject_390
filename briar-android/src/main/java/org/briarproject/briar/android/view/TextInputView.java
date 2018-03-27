package org.briarproject.briar.android.view;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.preference.PreferenceManager;
import android.support.annotation.CallSuper;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ImageButton;

import org.briarproject.bramble.api.db.DatabaseConfig;
import org.briarproject.briar.R;
import org.briarproject.briar.android.BriarApplication;
import org.briarproject.briar.android.BriarService;
import org.thoughtcrime.securesms.components.KeyboardAwareLinearLayout;
import org.thoughtcrime.securesms.components.emoji.EmojiDrawer;
import org.thoughtcrime.securesms.components.emoji.EmojiDrawer.EmojiEventListener;
import org.thoughtcrime.securesms.components.emoji.EmojiEditText;
import org.thoughtcrime.securesms.components.emoji.EmojiToggle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

@UiThread
public class TextInputView extends KeyboardAwareLinearLayout implements EmojiEventListener {

	protected final ViewHolder ui;
	protected TextInputListener listener;
    @Inject
    protected DatabaseConfig databaseConfig;

	public static final int ATTACH_IMAGES = 1;

	public TextInputView(Context context) {
		this(context, null);
	}

	public TextInputView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TextInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setOrientation(VERTICAL);
		setLayoutTransition(new LayoutTransition());

		inflateLayout(context);
		ui = new ViewHolder();
		setUpViews(context, attrs);
	}

	protected void inflateLayout(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.text_input_view, this, true);
	}

	@CallSuper
	protected void setUpViews(Context context, @Nullable AttributeSet attrs) {
		// get attributes
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TextInputView);
		String hint = attributes.getString(R.styleable.TextInputView_hint);
		attributes.recycle();

		if (hint != null) {
			ui.editText.setHint(hint);
		}

		ui.emojiToggle.attach(ui.emojiDrawer);
		ui.emojiToggle.setOnClickListener(v -> onEmojiToggleClicked());

		ui.editText.setOnClickListener(v -> showSoftKeyboard());
		ui.editText.setOnKeyListener((v, keyCode, event) -> {
			if (keyCode == KEYCODE_BACK) {
			    if(isEmojiDrawerOpen()) {
			        hideEmojiDrawer();
                    return true;
                }
			}
			if (keyCode == KEYCODE_ENTER) {
			    if(event.isCtrlPressed()) {
                    trySendMessage();
                    return true;
                }
			}
			return false;
		});

		ui.sendButton.setOnClickListener(v -> trySendMessage());
		ui.emojiDrawer.setEmojiEventListener(this);
	}

	private void trySendMessage() {
		String message = "";

		if (listener != null) {
			List<SelectedMediaView> selectedMedia = getSelectedMedia();

			if(selectedMedia.size() > 0) {
				ByteArrayOutputStream boas = new ByteArrayOutputStream();

				for(SelectedMediaView media: selectedMedia) {
					if(
						media.getType().equals("image/jpg") ||
							media.getType().equals("image/jpeg") ||
							media.getType().equals("image/png")) {
						Bitmap bitmap = media.getImage();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 30, boas);
					}

					byte[] mediaBytes = boas.toByteArray();

					message += "%shim%ImageTag:" + Base64.encodeToString(mediaBytes, android.util.Base64.DEFAULT);
				}
			}

			listener.onSendClick(getText().toString() + message);
		}
	}

	@Override
	public void setVisibility(int visibility) {
		if (visibility == GONE && isKeyboardOpen()) {
			onKeyboardClose();
		}

		super.setVisibility(visibility);
	}

	@Override
	public void onKeyEvent(KeyEvent keyEvent) {
		ui.editText.dispatchKeyEvent(keyEvent);
	}

	@Override
	public void onEmojiSelected(String emoji) {
		ui.editText.insertEmoji(emoji);
	}

	@Override
	public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
		return ui.editText.requestFocus(direction, previouslyFocusedRect);
	}

	private void onEmojiToggleClicked() {
		if (isEmojiDrawerOpen()) {
			showSoftKeyboard();
		} else {
			showEmojiDrawer();
		}
	}

    //send the base64 image String
    public void sendImage(String imageString){
	    ui.editText.setText("ImageTag:" + imageString);
	    trySendMessage();
	}

	public void setText(String text) {
        ui.editText.setText(text);
	}

	public Editable getText() {
		return ui.editText.getText();
	}

	public void addMedia(Uri mediaUri) {
        if (!ui.selectedMediaDrawer.isShown()) {
            ui.selectedMediaDrawer.setVisibility(VISIBLE);
        }

		SelectedMediaView media = new SelectedMediaView(getContext());

		String mediaType = getContext().getContentResolver().getType(mediaUri);
		media.setUri(mediaUri);
		media.setType(mediaType);

		Bitmap bitmap = null;
		ByteArrayOutputStream boas = new ByteArrayOutputStream();

		try {
			bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mediaUri);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(mediaType.equals("image/jpg") || mediaType.equals("image/jpeg")) {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas);
			media.setImage(bitmap);
		} else if(mediaType.equals("image/png")) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, boas);
			media.setImage(bitmap);
		}

        ui.selectedMediaDrawer.addView(media);
		ui.editText.requestFocus();
	}

	public List<SelectedMediaView> getSelectedMedia() {
		List<SelectedMediaView> selectedMedia = new ArrayList<>();

		for(int i = 0; i < ui.selectedMediaDrawer.getChildCount(); i++) {
			selectedMedia.add(((SelectedMediaView) ui.selectedMediaDrawer.getChildAt(i)));
		}

		return selectedMedia;
	}

	public void clearSelectedMediaDrawer() {
		ui.selectedMediaDrawer.removeAllViews();
	}

	public void setHint(@StringRes int res) {
		ui.editText.setHint(res);
	}

	public void setSendButtonEnabled(boolean enabled) {
		ui.sendButton.setEnabled(enabled);
	}

	public void addTextChangedListener(TextWatcher watcher) {
		ui.editText.addTextChangedListener(watcher);
	}

	public void setListener(TextInputListener listener) {
		this.listener = listener;
	}

	public void showSoftKeyboard() {
		if (isKeyboardOpen()) return;

		if (ui.emojiDrawer.isShowing()) {
			postOnKeyboardOpen(this::hideEmojiDrawer);
		}
		ui.editText.post(() -> {
			ui.editText.requestFocus();
			InputMethodManager imm = (InputMethodManager)
					getContext().getSystemService(INPUT_METHOD_SERVICE);
			imm.showSoftInput(ui.editText, SHOW_IMPLICIT);
		});
	}

	public void hideSoftKeyboard() {
		IBinder token = ui.editText.getWindowToken();
		Object o = getContext().getSystemService(INPUT_METHOD_SERVICE);
		((InputMethodManager) o).hideSoftInputFromWindow(token, 0);
	}

	public void showEmojiDrawer() {
		if (isKeyboardOpen()) {
			postOnKeyboardClose(() -> ui.emojiDrawer.show(getKeyboardHeight()));
			hideSoftKeyboard();
		} else {
			ui.emojiDrawer.show(getKeyboardHeight());
			ui.editText.requestFocus();
		}
	}

	public void hideEmojiDrawer() {
		ui.emojiDrawer.hide();
	}

	public boolean isEmojiDrawerOpen() {
		return ui.emojiDrawer.isShowing();
	}

	protected class ViewHolder {
		private ImageButton imageButton;
		private final EmojiToggle emojiToggle;

        final View sendButton;
		final EmojiEditText editText;
		final EmojiDrawer emojiDrawer;
		final LinearLayout selectedMediaDrawer;

		private ViewHolder() {
			imageButton = findViewById(R.id.open_image_browser);
			imageButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					((Activity)getContext()).startActivityForResult(
							Intent.createChooser(intent, "Select Media"),
							ATTACH_IMAGES
					);
				}
			});

			emojiToggle = findViewById(R.id.emoji_toggle);
            sendButton = findViewById(R.id.btn_send);
			editText = findViewById(R.id.input_text);
			emojiDrawer = findViewById(R.id.emoji_drawer);
			selectedMediaDrawer = findViewById(R.id.selected_media_container);
		}
	}

	public interface TextInputListener {
		void onSendClick(String text);
	}
}
