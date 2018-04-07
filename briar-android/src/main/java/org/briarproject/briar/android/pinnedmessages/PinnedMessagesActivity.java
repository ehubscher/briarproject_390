package org.briarproject.briar.android.pinnedmessages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;

import android.widget.RelativeLayout;

import org.briarproject.bramble.api.contact.ContactId;

import org.briarproject.briar.R;
import org.briarproject.briar.api.messaging.MessagingManager;
import org.thoughtcrime.securesms.components.emoji.EmojiTextView;


import javax.inject.Inject;

public class PinnedMessagesActivity extends AppCompatActivity {

    public static final String PINNED_MESSAGES = "briar.PINNED_MESSAGES";

    @Inject
    volatile MessagingManager messagingManager;

    private volatile ContactId contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String[] messages = intent.getStringArrayExtra(PINNED_MESSAGES);

        setContentView(R.layout.activity_pinned_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Add pinned messages to the layout
        if(this.findViewById(R.id.layout_pinned) != null) {
            RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.layout_pinned);

            for (int i = 0; i < messages.length; i++) {
                EmojiTextView text = new EmojiTextView(this);
                text.setText(messages[i]);

                layout.addView(text);

                //TODO display the messages here this is only for test
                /*AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Test");
                alertDialog.setMessage(messages[i]);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }

}
