package com.briar.server.resources;

import com.briar.server.mapper.ArticleMapper;
import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.Article;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.returnedtobriarclasses.BriarArticle;
import com.briar.server.services.ArticleService;
import com.briar.server.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/")
@Api
public class ArticleResource {
    private ArticleMapper articleMapper;
    private UserMapper userMapper;
    private ArticleService articleService;
    private UserService userService;

    public ArticleResource(ArticleMapper articleMapper, UserMapper userMapper){
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
        this.articleService = new ArticleService(articleMapper);
        this.userService = new UserService(userMapper);
    }

    /**
     * POST
     * Route: /users/{userId}/article
     * Payload:
     *       {
     *          "author": "jasmine",
     *          "publicationDate": "January 15, 2018",
     *          "title": "article title3",
     *          "body": [
     *                      "paragraph1",
     *                      "paragraph2",
     *                      "paragraph3",
     *                      "lastParagraph"
     *                  ]
     *       }
     * return value: 200 OK
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postArticle(BriarArticle briarArticle, @PathParam("userId") String phoneGeneratedId) {
        Response response;

        boolean userExists = this.userService.doesUserExists(phoneGeneratedId);
        if (!userExists) {
            // You can't update a user if it isn't created
            response = Response.status(Response.Status.NOT_FOUND).build();
            System.out.println(response);
            return response;
        }

        boolean isArticleValid = this.articleService.validateArticle(briarArticle);
        if(!isArticleValid){
            response = Response.status(Response.Status.BAD_REQUEST).build();
            System.out.println(response);
            return response;
        }

        Article article;
        try {
            User user = this.userService.readUser(phoneGeneratedId);
            article = this.articleService.convertBriarArticleToArticle(briarArticle, user);
            this.articleService.postArticle(article);
        } catch (Exception e) {
            System.out.println(e);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            System.out.println(response);
            return response;
        }

        response = Response.status(Response.Status.CREATED).build();
        System.out.println(response);
        return response;
    }
}
