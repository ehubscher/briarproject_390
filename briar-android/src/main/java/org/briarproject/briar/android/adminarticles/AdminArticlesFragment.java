package org.briarproject.briar.android.adminarticles;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.briarproject.bramble.api.contact.ContactId;
import org.briarproject.bramble.api.event.EventBus;
import org.briarproject.bramble.api.event.EventListener;
import org.briarproject.bramble.api.plugin.ConnectionRegistry;
import org.briarproject.bramble.plugin.tcp.IdContactHash;
import org.briarproject.bramble.restClient.BServerServicesImpl;
import org.briarproject.bramble.restClient.ServerObj.AllArticles;
import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.contact.BaseContactListAdapter;
import org.briarproject.briar.android.contact.ContactListAdapter;
import org.briarproject.briar.android.contact.ContactListFragment;
import org.briarproject.briar.android.contact.ContactListItem;
import org.briarproject.briar.android.contact.ContactListItemViewHolder;
import org.briarproject.briar.android.contact.ConversationActivity;
import org.briarproject.briar.android.fragment.BaseFragment;
import org.briarproject.briar.android.view.BriarRecyclerView;
import org.briarproject.briar.api.android.AndroidNotificationManager;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;
import static android.support.v4.view.ViewCompat.getTransitionName;
import static org.briarproject.briar.android.contact.ConversationActivity.CONTACT_ID;


/**
 * A placeholder fragment containing a simple view.
 */
public class AdminArticlesFragment extends BaseFragment implements EventListener {
    public static final String TAG = AdminArticlesFragment.class.getName();
    private static final Logger LOG = Logger.getLogger(TAG);
    private volatile HashMap<String, SavedUser> contactsDetails;
    private volatile IdContactHash contactsIdName;

    @Inject
    ConnectionRegistry connectionRegistry;
    @Inject
    EventBus eventBus;
    @Inject
    AndroidNotificationManager notificationManager;

    private ContactListAdapter adapter;
    private BriarRecyclerView list;
    public AdminArticlesFragment() {
    }

    public static AdminArticlesFragment newInstance(){
        Bundle args = new Bundle();
        AdminArticlesFragment fragment = new AdminArticlesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Admin Articles");
        return inflater.inflate(R.layout.fragment_admin_articles, container, false);
    }

    @Override
    public String getUniqueTag() {
        return TAG;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void injectFragment(ActivityComponent component) {
        component.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        //BServerServicesImpl webServer = new BServerServicesImpl();

        //if(webServer.getOrUpdateAllArticles()) {
            AllArticles.getInstanceAllArticles();
        //}
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle(R.string.contact_list_button);

        View contentView = inflater.inflate(R.layout.list, container, false);

        BaseArticleListAdapter.OnArticleClickListener<ArticleItem> onArticleClickListener = (view, item) -> {
            Intent i = new Intent(getActivity(), AdminArticlesActivity.class);
            if (Build.VERSION.SDK_INT >= 23) {
                ArticleItemViewHolder viewHolder = (ArticleItemViewHolder) list
                        .getRecyclerView()
                        .findViewHolderForAdapterPosition(adapter.findItemPosition(item));

                ActivityCompat.startActivity(getActivity(), i, options.toBundle());
            } else {
                // work-around for android bug #224270
                startActivity(i);
            }
        };

        adapter = new ArticleListAdapter(getContext(), onArticleClickListener);
        list = contentView.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
        list.setEmptyText(getString(R.string.no_contacts));

        return contentView;
    }
}
