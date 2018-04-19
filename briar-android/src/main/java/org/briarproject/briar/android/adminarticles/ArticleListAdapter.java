package org.briarproject.briar.android.adminarticles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.briarproject.briar.R;

import javax.annotation.Nullable;

public class ArticleListAdapter extends BaseArticleListAdapter<ArticleListItem, ArticleListItemViewHolder> {
    public ArticleListAdapter(Context ctx, OnArticleClickListener<ArticleListItem> listener) {
        super(ctx, ArticleListItem.class, listener);
    }

    @Override
    public ArticleListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View articleListItem = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_item_article,
                viewGroup,
                false
        );

        return new ArticleListItemViewHolder(articleListItem);
    }
}
