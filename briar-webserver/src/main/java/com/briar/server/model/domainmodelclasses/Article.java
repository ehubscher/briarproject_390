package com.briar.server.model.domainmodelclasses;

import java.util.Date;
import java.util.Objects;

public class Article {

    Date publicationDate;
    String author;
    String title;
    String body;
    long addedBy;

    public Article() {

    }

    public Article(Date publicationDate, String author, String title, String body, long addedBy) {
        this.publicationDate = publicationDate;
        this.author = author;
        this.title = title;
        this.body = body;
        this.addedBy = addedBy;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(long addedBy) {
        this.addedBy = addedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return addedBy == article.addedBy &&
                Objects.equals(publicationDate, article.publicationDate) &&
                Objects.equals(author, article.author) &&
                Objects.equals(title, article.title) &&
                Objects.equals(body, article.body);
    }

    @Override
    public int hashCode() {

        return Objects.hash(publicationDate, author, title, body, addedBy);
    }

    @Override
    public String toString() {
        return "Article{" +
                "publicationDate=" + publicationDate +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", addedBy=" + addedBy +
                '}';
    }
}
