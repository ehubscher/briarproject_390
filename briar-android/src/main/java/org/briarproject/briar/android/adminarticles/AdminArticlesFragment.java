package org.briarproject.briar.android.adminarticles;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.briarproject.bramble.api.event.Event;
import org.briarproject.bramble.api.event.EventListener;
import org.briarproject.bramble.api.nullsafety.MethodsNotNullByDefault;
import org.briarproject.bramble.api.nullsafety.ParametersNotNullByDefault;
import org.briarproject.bramble.restClient.ServerObj.AllArticles;
import org.briarproject.bramble.restClient.ServerObj.Article;
import org.briarproject.briar.R;
import org.briarproject.briar.android.activity.ActivityComponent;
import org.briarproject.briar.android.fragment.BaseFragment;
import org.briarproject.briar.android.view.BriarRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;

@MethodsNotNullByDefault
@ParametersNotNullByDefault
public class AdminArticlesFragment extends BaseFragment implements EventListener, BaseArticleListAdapter.OnArticleClickListener {
    public static final String TAG = AdminArticlesFragment.class.getName();
    private static final Logger LOG = Logger.getLogger(TAG);

    private ArticleListAdapter adapter = new ArticleListAdapter(getActivity(), this);
    private BriarRecyclerView list;

    public static AdminArticlesFragment newInstance() {
        Bundle args = new Bundle();
        AdminArticlesFragment fragment = new AdminArticlesFragment();
        fragment.setArguments(args);
        return fragment;
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

        List<Article> articles = AllArticles.getInstanceAllArticles();
        if(articles.size() > 0) {

            /*
            for(Article article : articles) {
                adapter.add(new ArticleListItem(article));
            }

            */
            List<ArticleListItem> articleListItems = new ArrayList<>();
            for(Article article : articles) {
                articleListItems.add(new ArticleListItem(article));
            }
            adapter.addAll(articleListItems);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Articles");
        View articleList = inflater.inflate(R.layout.fragment_admin_articles, container, false);

        list = articleList.findViewById(R.id.articlesList);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
        list.showProgressBar();
        list.setEmptyText(getString(R.string.no_admin_articles));

        return articleList;
    }

    @Override
    public void eventOccurred(Event e) {}

    @Override
    public void onItemClick(View view, Object item) {

    }
}
