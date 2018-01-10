package org.briarproject.briar.android.privategroup.memberlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.briarproject.briar.R;
import org.briarproject.briar.android.util.BriarAdapter;

class MemberListAdapter extends
		BriarAdapter<MemberListItem, MemberListItemHolder> {

	MemberListAdapter(Context context) {
		super(context, MemberListItem.class);
	}

	@Override
	public MemberListItemHolder onCreateViewHolder(ViewGroup viewGroup,
			int i) {
		View v = LayoutInflater.from(ctx).inflate(
				R.layout.list_item_group_member, viewGroup, false);
		return new MemberListItemHolder(v);
	}

	@Override
	public void onBindViewHolder(MemberListItemHolder ui, int position) {
		ui.bind(items.get(position));
	}

	@Override
	public int compare(MemberListItem m1, MemberListItem m2) {
		return m1.getMember().getName().compareTo(m2.getMember().getName());
	}

	@Override
	public boolean areContentsTheSame(MemberListItem m1, MemberListItem m2) {
		if (m1.isOnline() != m2.isOnline()) return false;
		if (m1.getContactId() != m2.getContactId()) return false;
		if (m1.getStatus() != m2.getStatus()) return false;
		return true;
	}

	@Override
	public boolean areItemsTheSame(MemberListItem m1, MemberListItem m2) {
		return m1.getMember().equals(m2.getMember());
	}

}
