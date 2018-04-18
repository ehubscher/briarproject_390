package org.briarproject.briar.android.adminarticles;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.briarproject.bramble.restClient.BServerServicesImpl;
import org.briarproject.bramble.restClient.ServerObj.AllArticles;
import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.fragment.BaseFragment;


/**
 * A placeholder fragment containing a simple view.
 */
public class AdminArticlesFragment extends BaseFragment {

    public AdminArticlesFragment() {
    }

    public static AdminArticlesFragment newInstance(){
        return new AdminArticlesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Admin Articles");
        return inflater.inflate(R.layout.fragment_admin_articles, container, false);
    }

    @Override
    public String getUniqueTag() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void injectFragment(ActivityComponent component) {

    }

    @Override
    public void onStart() {
        super.onStart();

        BServerServicesImpl webServer = new BServerServicesImpl();

        if(webServer.getOrUpdateAllArticles()) {
            AllArticles.getInstanceAllArticles();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
