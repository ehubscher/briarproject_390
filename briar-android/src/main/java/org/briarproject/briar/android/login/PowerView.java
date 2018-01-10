package org.briarproject.briar.android.login;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.briar.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static org.briarproject.briar.android.util.UiUtils.showOnboardingDialog;

@UiThread
@NotNullByDefault
abstract class PowerView extends ConstraintLayout {

	private final TextView textView;
	private final ImageView checkImage;
	private final Button button;

	private boolean checked = false;

	@Nullable
	private OnCheckedChangedListener onCheckedChangedListener;

	public PowerView(Context context) {
		this(context, null);
	}

	public PowerView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressWarnings("ConstantConditions")
	public PowerView(Context context, @Nullable AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.power_view, this, true);

		textView = v.findViewById(R.id.textView);
		checkImage = v.findViewById(R.id.checkImage);
		button = v.findViewById(R.id.button);
		button.setOnClickListener(view -> onButtonClick());
		ImageButton helpButton = v.findViewById(R.id.helpButton);
		helpButton.setOnClickListener(view -> onHelpButtonClick());

		// we need to manage the checkImage state ourselves, because automatic
		// state saving is done based on the view's ID and there can be
		// multiple ImageViews with the same ID in the view hierarchy
		setSaveFromParentEnabled(true);

		if (!isInEditMode() && !needsToBeShown()) {
			setVisibility(GONE);
		}
	}

	@Nullable
	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.value = new boolean[] {checked};
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		setChecked(ss.value[0]);  // also calls listener
	}

	public abstract boolean needsToBeShown();

	public void setChecked(boolean checked) {
		this.checked = checked;
		if (checked) {
			checkImage.setImageResource(R.drawable.ic_check_white);
		} else {
			checkImage.setImageResource(R.drawable.contact_disconnected);
		}
		if (onCheckedChangedListener != null) {
			onCheckedChangedListener.onCheckedChanged();
		}
	}

	public boolean isChecked() {
		return getVisibility() == GONE || checked;
	}

	public void setOnCheckedChangedListener(
			OnCheckedChangedListener onCheckedChangedListener) {
		this.onCheckedChangedListener = onCheckedChangedListener;
	}

	@StringRes
	protected abstract int getHelpText();

	protected void setText(@StringRes int res) {
		textView.setText(res);
	}

	protected void setButtonText(@StringRes int res) {
		button.setText(res);
	}

	protected abstract void onButtonClick();

	private void onHelpButtonClick() {
		showOnboardingDialog(getContext(),
				getContext().getString(getHelpText()));
	}

	private static class SavedState extends BaseSavedState {
		private boolean[] value = {false};

		private SavedState(@Nullable Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			in.readBooleanArray(value);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(value);
		}

		static final Parcelable.Creator<SavedState> CREATOR
				= new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	interface OnCheckedChangedListener {
		void onCheckedChanged();
	}

}
