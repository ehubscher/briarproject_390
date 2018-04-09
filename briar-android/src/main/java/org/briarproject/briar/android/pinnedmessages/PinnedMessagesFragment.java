package org.briarproject.briar.android.pinnedmessages;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.briarproject.briar.R;
import org.thoughtcrime.securesms.components.emoji.EmojiTextView;


public class PinnedMessagesFragment extends Fragment {

    public PinnedMessagesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Find relative layout
        View view = inflater.inflate(R.layout.fragment_pinned_messages, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.layout_pinned);

        //Create linear layout
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        PinnedMessagesActivity messagesActivity = (PinnedMessagesActivity) getActivity();

        //No messages pinned
        if(messagesActivity.messages.length==0){
           TextView noMessages = view.findViewById(R.id.emptyView);
           noMessages.setVisibility(View.VISIBLE);
        }//Some messages are pinned
        else{
            for (int i = 0; i < messagesActivity.messages.length; i++) {
                //TODO: Add timestamp before each messages
                //Add text
                EmojiTextView text = new EmojiTextView(this.getContext());
                text.setText(messagesActivity.messages[i]);
                text.setTextSize(16);
                text.setPadding(20,20,20,20);
                linearLayout.addView(text);

                //Add divider between messages
                ImageView divider = new ImageView(this.getContext());
                LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2);
                layoutParam.setMargins(10, 15, 10, 15);
                divider.setLayoutParams(layoutParam);
                divider.setBackgroundColor(Color.LTGRAY);
                linearLayout.addView(divider);
            }
            relativeLayout.addView(linearLayout);//add linear layout to relative layout
        }
        return view;
    }
}
