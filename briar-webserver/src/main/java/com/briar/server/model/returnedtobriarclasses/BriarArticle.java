package com.briar.server.model.returnedtobriarclasses;

import java.util.Arrays;

public class BriarArticle {

    String author;
    String publicationDate;
    String title;
    String[] body;

    public BriarArticle() {
    }

    public BriarArticle(String author, String publicationDate,
                        String title, String[] body) {
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

    public String[] getBody() {
        return body;
    }

    public void setBody(String[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "BriarArticle{" +
                "author='" + author + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", title='" + title + '\'' +
                ", body=" + Arrays.toString(body) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BriarArticle)) {
            return false;
        }

        BriarArticle that = (BriarArticle) o;

        if (getAuthor() != null ? !getAuthor().equals(that.getAuthor()) :
                that.getAuthor() != null) {
            return false;
        }
        if (getPublicationDate() != null ?
                !getPublicationDate().equals(that.getPublicationDate()) :
                that.getPublicationDate() != null) {
            return false;
        }
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) :
                that.getTitle() != null) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getBody(), that.getBody());

    }

    @Override
    public int hashCode() {
        int result = getAuthor() != null ? getAuthor().hashCode() : 0;
        result = 31 * result +
                (getPublicationDate() != null ?
                        getPublicationDate().hashCode() :
                        0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getBody());
        return result;
    }
}
