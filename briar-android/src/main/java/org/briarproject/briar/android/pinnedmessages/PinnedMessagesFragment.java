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
        RelativeLayout layout = view.findViewById(R.id.layout_pinned);

        //Find linear layout
        LinearLayout lL = new LinearLayout(this.getContext());
        lL.setOrientation(LinearLayout.VERTICAL);

        PinnedMessagesActivity a = (PinnedMessagesActivity) getActivity();

        //No messages pinned
        if(a.messages.length==0){
           TextView noMessages = view.findViewById(R.id.emptyView);
           noMessages.setVisibility(View.VISIBLE);
        }//Some messages are pinned
        else{
            for (int i = 0; i < a.messages.length; i++) {
                //TODO: Add timestamp before each messages
                //Add text
                EmojiTextView text = new EmojiTextView(this.getContext());
                text.setText(a.messages[i]);
                text.setTextSize(16);
                text.setPadding(15,20,15,20);
                lL.addView(text);

                //Add divider between messages
                ImageView divider = new ImageView(this.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
                lp.setMargins(10, 10, 10, 10);
                divider.setLayoutParams(lp);
                divider.setBackgroundColor(Color.BLACK);
                lL.addView(divider);
            }
            layout.addView(lL);//add linear layout to relative layout
        }
        return view;
    }
}
