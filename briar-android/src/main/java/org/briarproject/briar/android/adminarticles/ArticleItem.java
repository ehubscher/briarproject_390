package org.briarproject.briar.android.adminarticles;

import org.briarproject.bramble.restClient.ServerObj.Article;

import java.util.List;

public class ArticleItem {
    protected final Article article;

    protected String author;
    protected String publicationDate;
    protected String title;
    protected List<String> body;

    public ArticleItem(Article article) {
        this.article = article;
        this.author = article.getAuthor();
        this.publicationDate = article.getPublicationDate();
        this.title = article.getTitle();
        this.body = article.getBody();
    }

    public Article getArticle() {
        return this.article;
    }
}
