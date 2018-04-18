package org.briarproject.briar.android.adminarticles;

import org.briarproject.bramble.restClient.ServerObj.Article;

public class ArticleItem {
    private final Article article;

    public ArticleItem(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return this.article;
    }
}
