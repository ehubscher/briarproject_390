package org.briarproject.briar.android.adminarticles;

import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;

import org.briarproject.bramble.api.nullsafety.NotNullByDefault;
import org.briarproject.briar.R;
import org.briarproject.briar.android.contact.BaseContactListAdapter;
import org.briarproject.briar.android.contact.ContactListItem;

import java.util.Date;

import javax.annotation.Nullable;

@UiThread
@NotNullByDefault
public class ArticleListItemViewHolder extends ArticleItemViewHolder<ArticleListItem> {
    private final TextView author;
    private final TextView title;
    private final TextView publicationDate;
    private final TextView body;

    ArticleListItemViewHolder(View v) {
        super(v);

        author = v.findViewById(R.id.author);
        title = v.findViewById(R.id.title);
        publicationDate = v.findViewById(R.id.publicationDate);
        body = v.findViewById(R.id.body);
    }

    @Override
    protected void bind(ArticleListItem item, @Nullable BaseArticleListAdapter.OnArticleClickListener<ArticleListItem> listener) {
        super.bind(item, listener);
        String articleBody = "";
        for(String paragraph : item.getBody()) {
            articleBody += paragraph + "\n\n";
        }

        author.setText(item.getAuthor());
        title.setText(item.getTitle());
        publicationDate.setText(item.getPublicationDate());
        body.setText(articleBody);
    }
}
