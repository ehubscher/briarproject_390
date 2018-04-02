package org.briarproject.briar.android.pinnedmessages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.briarproject.bramble.api.contact.ContactId;
import org.briarproject.bramble.api.db.DbException;
import org.briarproject.bramble.api.sync.MessageId;
import org.briarproject.briar.R;
import org.briarproject.briar.api.messaging.MessagingManager;

import java.util.Collection;

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

        for (int i = 0; i < messages.length; i++){
            //TODO display the messages here this is only for test
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
