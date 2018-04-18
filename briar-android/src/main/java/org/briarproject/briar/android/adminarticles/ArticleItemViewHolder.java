package org.briarproject.briar.android.adminarticles;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.briarproject.bramble.api.identity.Author;
import org.briarproject.bramble.plugin.tcp.ContactHash;
import org.briarproject.bramble.plugin.tcp.IdContactHash;
import org.briarproject.bramble.restClient.ServerObj.SavedUser;
import org.briarproject.briar.R;
import org.briarproject.briar.android.contact.BaseContactListAdapter;
import org.briarproject.briar.android.contact.ContactItemViewHolder;

import java.util.logging.Logger;

import javax.annotation.Nullable;

import im.delight.android.identicons.IdenticonDrawable;

public class ArticleItemViewHolder <I extends ArticleItem> extends RecyclerView.ViewHolder {
    protected final ViewGroup layout;

    private static final Logger LOG = Logger.getLogger(ContactItemViewHolder.class.getName());
    public ArticleItemViewHolder(View v) {
        super(v);

        layout = (ViewGroup) v;
    }

    protected void bind(I item, @Nullable BaseArticleListAdapter.OnArticleClickListener<I> listener) {
        

    }
}
