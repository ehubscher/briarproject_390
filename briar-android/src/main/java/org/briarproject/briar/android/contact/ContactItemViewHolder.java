package org.briarproject.briar.android.contact;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.briarproject.bramble.api.identity.Author;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.briar.R;
import org.briarproject.briar.android.contact.BaseContactListAdapter.OnContactClickListener;

import javax.annotation.Nullable;

import im.delight.android.identicons.IdenticonDrawable;

@UiThread
@NotNullByDefault
public class ContactItemViewHolder<I extends ContactItem>
		extends RecyclerView.ViewHolder {

	protected final ViewGroup layout;
	protected final ImageView avatar;
	protected final TextView name;
    private final TextView favourite_contact;
	@Nullable
	protected final ImageView bulb;

	public ContactItemViewHolder(View v) {
		super(v);

		layout = (ViewGroup) v;
		avatar = v.findViewById(R.id.avatarView);
		name = v.findViewById(R.id.nameView);
        favourite_contact = v.findViewById(R.id.favouriteView);
		// this can be null as not all layouts that use this ViewHolder have it
		bulb = v.findViewById(R.id.bulbView);
	}

	protected void bind(I item, @Nullable OnContactClickListener<I> listener) {
		Author author = item.getContact().getAuthor();
		String contactName = author.getName();
		name.setText(contactName);
		avatar.setImageDrawable(
				new IdenticonDrawable(author.getId().getBytes()));//to be removed later

		if (bulb != null) {
			// online/offline
			if (item.isConnected()) {
				bulb.setImageResource(R.drawable.contact_connected);
			} else {
				bulb.setImageResource(R.drawable.contact_disconnected);
			}
		}

		//TODO: Sets avatar to either custom avatar
		if(item.currentAvatar() !=0){
			//Use custom avatar
		}
		else{//Use Identicon by default
			avatar.setImageDrawable(
				new IdenticonDrawable(author.getId().getBytes()));
		}

		//Set visibility of the the star next to each conversation with contacts
        if(item.getContact().isFavourite()){
            favourite_contact.setVisibility(View.VISIBLE);
        }
        else{
            favourite_contact.setVisibility(View.GONE);
        }

		layout.setOnClickListener(v -> {
			if (listener != null) listener.onItemClick(avatar, item);
		});
	}

}
