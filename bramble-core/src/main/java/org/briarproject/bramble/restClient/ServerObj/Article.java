package org.briarproject.bramble.restClient.ServerObj;

import java.util.List;

/**
 * Created by Winterhart on 4/15/2018.
 * Article object used to stored article from server
 */

public class Article {
    private String author;
    private String publicationDate;
    private String title;
    private List<String> body;

    public Article(String author, String publicationDate, String title, List<String> body) {
        this.author = author;
        this.publicationDate = publicationDate;
        this.title = title;
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (author != null ? !author.equals(article.author) : article.author != null) return false;
        if (publicationDate != null ? !publicationDate.equals(article.publicationDate) : article.publicationDate != null)
            return false;
        if (title != null ? !title.equals(article.title) : article.title != null) return false;
        return body != null ? body.equals(article.body) : article.body == null;
    }
}
