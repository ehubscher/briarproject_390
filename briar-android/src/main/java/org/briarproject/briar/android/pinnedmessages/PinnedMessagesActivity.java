package org.briarproject.briar.android.pinnedmessages;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

    public static final String CONTACT_ID = "briar.CONTACT_ID";

    @Inject
    volatile MessagingManager messagingManager;

    private volatile ContactId contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        int id = i.getIntExtra(CONTACT_ID, -1);
        if (id == -1) throw new IllegalStateException();
        contactId = new ContactId(id);

        setContentView(R.layout.activity_pinned_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try{
            Collection<MessageId> listOfMessageId = messagingManager.getPinnedMessages(contactId);

            //Loop the list to get the body of each pinned messages
            for (MessageId messageId: listOfMessageId) {
                messagingManager.getMessageBody(messageId);
            }
        }catch (DbException e){
            //continue or display failed message here
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
