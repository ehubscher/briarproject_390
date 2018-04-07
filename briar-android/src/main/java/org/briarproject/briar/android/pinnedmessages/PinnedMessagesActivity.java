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

    String[] messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        messages = intent.getStringArrayExtra(PINNED_MESSAGES);

        setContentView(R.layout.activity_pinned_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
