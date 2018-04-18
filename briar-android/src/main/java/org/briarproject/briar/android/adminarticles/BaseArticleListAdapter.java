package org.briarproject.briar.android.adminarticles;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.briarproject.briar.android.util.BriarAdapter;

import javax.annotation.Nullable;

public class BaseArticleListAdapter<I extends ArticleItem, VH extends ArticleItemViewHolder<I>> extends BriarAdapter<I, VH>  {
    @Nullable
    protected final BaseArticleListAdapter.OnArticleClickListener<I> listener;

    public BaseArticleListAdapter(Context ctx, Class<I> c, @Nullable BaseArticleListAdapter.OnArticleClickListener<I> listener) {
        super(ctx, c);
        this.listener = listener;
    }


    @Override
    public int compare(I item1, I item2) {
        return 0;
    }

    @Override
    public boolean areContentsTheSame(I item1, I item2) {

        return false;
    }

    @Override
    public boolean areItemsTheSame(I item1, I item2) {
        return false;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {

    }

    public interface OnArticleClickListener<I> {
        void onItemClick(View view, I item);
    }
}
