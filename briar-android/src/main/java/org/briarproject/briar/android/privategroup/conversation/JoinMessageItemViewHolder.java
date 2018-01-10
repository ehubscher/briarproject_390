package org.briarproject.briar.android.privategroup.conversation;

import android.content.Context;
import android.support.annotation.UiThread;
import android.view.View;

import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.briar.R;
import org.briarproject.briar.android.threaded.BaseThreadItemViewHolder;
import org.briarproject.briar.android.threaded.ThreadItemAdapter.ThreadItemListener;

import static org.briarproject.bramble.api.identity.Author.Status.OURSELVES;

@UiThread
@NotNullByDefault
class JoinMessageItemViewHolder
		extends BaseThreadItemViewHolder<GroupMessageItem> {

	private final boolean isCreator;

	JoinMessageItemViewHolder(View v, boolean isCreator) {
		super(v);
		this.isCreator = isCreator;
	}

	@Override
	public void bind(GroupMessageItem item,
			ThreadItemListener<GroupMessageItem> listener) {
		super.bind(item, listener);

		if (isCreator) bindForCreator((JoinMessageItem) item);
		else bind((JoinMessageItem) item);
	}

	private void bindForCreator(JoinMessageItem item) {
		if (item.isInitial()) {
			textView.setText(R.string.groups_member_created_you);
		} else {
			textView.setText(
					getContext().getString(R.string.groups_member_joined,
							item.getAuthor().getName()));
		}
	}

	private void bind(JoinMessageItem item) {
		Context ctx = getContext();

		if (item.isInitial()) {
			textView.setText(ctx.getString(R.string.groups_member_created,
					item.getAuthor().getName()));
		} else {
			if (item.getStatus() == OURSELVES) {
				textView.setText(R.string.groups_member_joined_you);
			} else {
				textView.setText(ctx.getString(R.string.groups_member_joined,
						item.getAuthor().getName()));
			}
		}
	}

}
