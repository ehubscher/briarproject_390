package org.briarproject.briar.android.adminarticles;

import android.content.Context;

import javax.annotation.Nullable;

public class ArticleListAdapter extends BaseArticleListAdapter<ArticleItem, ArticleItemViewHolder> {
    public ArticleListAdapter(Context ctx, OnArticleClickListener<ArticleItem> listener) {
        super(ctx, ArticleItem.class, listener);
    }
}
