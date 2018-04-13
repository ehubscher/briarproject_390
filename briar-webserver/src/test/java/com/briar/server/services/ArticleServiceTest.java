package com.briar.server.services;

import com.briar.server.mapper.ArticleMapper;
import com.briar.server.model.domainmodelclasses.Article;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.returnedtobriarclasses.BriarArticle;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.mockito.Mockito.mock;

public class ArticleServiceTest {

    static ArticleService articleService;
    BriarArticle briarArticle;
    Article article;
    String[] body;
    String publicationDateStr;
    User user;

    @BeforeClass
    public static void setupClass() {
        ArticleMapper mockArticleMapper = mock(ArticleMapper.class);
        articleService = new ArticleService(mockArticleMapper);
    }

    @Before
    public void beforeEachTest() {
        this.briarArticle = new BriarArticle();
        body = new String[]{"paragraph1", "paragraph2", "paragraph3", "final " +
                "paragraph"};
        this.publicationDateStr = "December 12, 2005";

        this.briarArticle.setAuthor("Benjamin");
        this.briarArticle.setBody(body);
        this.briarArticle.setPublicationDate(publicationDateStr);
        this.briarArticle.setTitle("Title!!!");

        this.article = new Article();
        this.article.setTitle("Title!!!");
        this.article.setBody("paragraph1%#%paragraph2%#%paragraph3%#%final " +
                "paragraph");

        long id = 14;

        this.article.setAddedBy(id);
        this.article.setAuthor("Benjamin");
        Date date;
        if (SystemUtils.IS_OS_LINUX){
            date = new Date(1134345600000L);
        }
        else{
            date = new Date(1134363600000L);
        }
        this.article.setPublicationDate(date);

        this.user = new User();
        this.user.setId(id);
    }

    @Test
    public void convertBriarArticleToArticleTest()
            throws ParseException {
        Article outputArticle = articleService.convertBriarArticleToArticle
                (this.briarArticle,
                this.user);
        assert(outputArticle.equals(this.article));
    }

    @Test
    public void convertArticleToBriarArticleTest() {
        BriarArticle outputArticle = articleService
                .convertArticleToBriarArticle(this.article);
        assert(outputArticle.equals(this.briarArticle));
    }

    @Test
    public void validateBriarArticleHappyPath() {
        assert(articleService.validateArticle(this.briarArticle));
    }

    @Test
    public void validateBriarArticleAuthorNull() {
        this.briarArticle.setAuthor(null);
        assert(!articleService.validateArticle(this.briarArticle));
    }

    @Test
    public void validateBriarArticleAuthorEmpty() {
        this.briarArticle.setAuthor("");
        assert(!articleService.validateArticle(this.briarArticle));
    }

    @Test
    public void validateBriarArticleTitleNull() {
        this.briarArticle.setTitle(null);
        assert(!articleService.validateArticle(this.briarArticle));
    }

    @Test
    public void validateBriarArticleTitleEmpty() {
        this.briarArticle.setTitle("");
        assert(!articleService.validateArticle(this.briarArticle));
    }

    @Test
    public void validateBriarArticleBodyNull() {
        this.briarArticle.setBody(null);
        assert(!articleService.validateArticle(this.briarArticle));
    }

    @Test
    public void validateBriarArticleOneParagraphEmpty() {
        String[] newBody = new String[]{"paragraph1", "", "paragraph3", "final " +
                "paragraph"};
        this.briarArticle.setBody(newBody);
        assert(!articleService.validateArticle(this.briarArticle));
    }

    @Test
    public void validateBriarArticleOneParagraphNullEmpty() {
        String[] newBody = new String[]{"paragraph1", null, "paragraph3",
                "final " +
                "paragraph"};
        this.briarArticle.setBody(newBody);
        assert(!articleService.validateArticle(this.briarArticle));
    }

    @Test
    public void validateBriarArticleDateIncorrect() {
        // Inserting typo to check that it breaks
        this.briarArticle.setPublicationDate("Decembr 14, 1990");
        assert(!articleService.validateArticle(this.briarArticle));
    }

    @Test
    public void validateBriarArticleGoodDateWrongOrder() {
        // Inserting typo to check that it breaks
        this.briarArticle.setPublicationDate("14 December, 1990");
        assert(!articleService.validateArticle(this.briarArticle));
    }


}
