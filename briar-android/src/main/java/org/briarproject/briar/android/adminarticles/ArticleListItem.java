package org.briarproject.briar.android.adminarticles;

import org.briarproject.bramble.restClient.ServerObj.Article;

import java.util.List;

public class ArticleListItem extends ArticleItem {
    public ArticleListItem(Article article) {
        super(article);
    }

    public String getAuthor() {
        return super.author;
    }

    public String getPublicationDate() {
        return super.publicationDate;
    }

    public String getTitle() {
        return super.title;
    }

    public List<String> getBody() {
        return super.body;
    }
}
