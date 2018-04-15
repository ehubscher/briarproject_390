package com.briar.server.services;

import com.briar.server.mapper.ArticleMapper;
import com.briar.server.model.domainmodelclasses.Article;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.returnedtobriarclasses.BriarArticle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleService {

    private ArticleMapper articleMapper;

    public ArticleService(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    public List<BriarArticle> convertAllArticles(List<Article> articles){
        List<BriarArticle> briarArticles = new ArrayList<>();
        for(Article article: articles) {
            briarArticles.add(convertArticleToBriarArticle
                    (article));
        }
        return briarArticles;
    }

    public BriarArticle convertArticleToBriarArticle(Article article) {
        String author = article.getAuthor();
        String publicationDate = parseDate(article.getPublicationDate());
        String title = article.getTitle();
        String[] body = parseBody(article.getBody());
        return new BriarArticle(author, publicationDate, title, body);
    }

    public Article convertBriarArticleToArticle(BriarArticle briarArticle, User user) throws ParseException {
        Article article = new Article();
        Date date = reverseParseDate(briarArticle.getPublicationDate());
        String body = reverseParseBody(briarArticle.getBody());

        article.setPublicationDate(date);
        article.setAuthor(briarArticle.getAuthor());
        article.setTitle(briarArticle.getTitle());
        article.setBody(body);
        article.setAddedBy(user.getId());

        return article;
    }

    private static String parseDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

    private static String[] parseBody(String string) {
        return string.split("%#%");
    }

    private static Date reverseParseDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");

        return simpleDateFormat.parse(date);
    }

    private static String reverseParseBody(String[] body){
        return String.join("%#%", body);
    }

    public boolean validateArticle(BriarArticle briarArticle){
        String author = briarArticle.getAuthor();
        String title = briarArticle.getTitle();
        String[] body = briarArticle.getBody();

        boolean hasDate = true;
        boolean hasAuthor = true;
        boolean hasTitle = true;
        boolean hasBody = true;

        try {
            reverseParseDate(briarArticle.getPublicationDate());
        } catch (ParseException e) {
            hasDate = false;
        }

        if (author == null || author.isEmpty()){
            hasAuthor = false;
        }

        if (title == null || title.isEmpty()){
            hasTitle = false;
        }

        if (body != null) {
            for (String content : body) {
                if (content == null || content.isEmpty()) {
                    hasBody = false;
                }
            }
        }
        else {
            hasBody = false;
        }

        return hasDate && hasAuthor && hasTitle && hasBody;
    }

    public void postArticle(Article article){
        this.articleMapper.addArticle(article);
    }

    public List<Article> retrieveAllArticles(){
        return this.articleMapper.retrieveAllArticles();
    }
}
