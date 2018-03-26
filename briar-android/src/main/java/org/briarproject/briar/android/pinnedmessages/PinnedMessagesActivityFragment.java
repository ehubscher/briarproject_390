package org.briarproject.briar.android.pinnedmessages;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.briarproject.briar.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class PinnedMessagesActivityFragment extends Fragment {

    public PinnedMessagesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pinned_messages, container, false);
    }

}
