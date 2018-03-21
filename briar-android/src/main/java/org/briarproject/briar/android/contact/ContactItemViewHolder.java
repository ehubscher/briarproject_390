package org.briarproject.briar.android.contact;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.briarproject.bramble.api.identity.Author;
import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.bramble.restClient.BServerServicesImpl;
import org.briarproject.bramble.restClient.ServerObj.SavedUser;
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

	BServerServicesImpl briarServices = new BServerServicesImpl();
	SavedUser targetContactUserInfo;

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
        targetContactUserInfo = briarServices.obtainUserInfo(contactName);
		avatar.setImageDrawable(
				new IdenticonDrawable(author.getId().getBytes()));//to be removed later

		//Set status
        int status;

        try{
            status = targetContactUserInfo.getAvatarId();
        }catch (Exception e){
            status = 1;
        }

		if (bulb != null) {
			// online/offline
			if (item.isConnected()) {
				if(status==1)
					bulb.setImageResource(R.drawable.contact_connected);
				else if(status==2)
					bulb.setImageResource(R.drawable.contact_busy);
				else
					bulb.setImageResource(R.drawable.contact_disconnected);

			} else {
				bulb.setImageResource(R.drawable.contact_disconnected);
			}
		}

		int avatarId;

		try{
            avatarId = targetContactUserInfo.getAvatarId();
        }catch (Exception e){
		    avatarId = 99;
        }

		if(avatarId != 99 && avatarId < 9){
			int imageNb = avatarId - 1;
			// references to our images
			Integer[] mThumbIds = {
					R.drawable.pig,
					R.drawable.panda,
					R.drawable.dog,
					R.drawable.cat,
					R.drawable.bunny,
					R.drawable.monkey,
					R.drawable.frog,
					R.drawable.penguin,
					R.drawable.robot
			};
			avatar.setImageResource(mThumbIds[imageNb]);
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
