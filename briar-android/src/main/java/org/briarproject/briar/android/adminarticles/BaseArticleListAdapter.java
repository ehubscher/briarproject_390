package org.briarproject.briar.android.adminarticles;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.briarproject.briar.android.util.BriarAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

public abstract class BaseArticleListAdapter<I extends ArticleItem, VH extends ArticleItemViewHolder<I>> extends BriarAdapter<I, VH>  {
    @Nullable
    protected final BaseArticleListAdapter.OnArticleClickListener<I> listener;

    public BaseArticleListAdapter(Context ctx, Class<I> c, @Nullable BaseArticleListAdapter.OnArticleClickListener<I> listener) {
        super(ctx, c);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        I item = items.get(position);
        viewHolder.bind(item, listener);
    }

    @Override
    public int compare(I item1, I item2) {
        int result = 0;
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

        try {
            Date date1 = format.parse(item1.publicationDate);
            Date date2 = format.parse(item2.publicationDate);

            if(date1.after(date2)) {
                result = 1;
            } else if(date1.before(date2)) {
                result = -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    @Override
    public boolean areContentsTheSame(I item1, I item2) {
        return (item1.body.equals(item2.body));
    }

    @Override
    public boolean areItemsTheSame(I item1, I item2) {
        return (item1.equals(item2));
    }

    public interface OnArticleClickListener<I> {
        void onItemClick(View view, I item);
    }
}
