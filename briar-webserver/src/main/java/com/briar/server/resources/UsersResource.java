package com.briar.server.resources;

import com.briar.server.mapper.ArticleMapper;
import com.briar.server.mapper.UserContactMapper;
import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.Article;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.returnedtobriarclasses.BriarProfileUser;
import com.briar.server.model.returnedtobriarclasses.BriarUser;
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
public class UsersResource {

    private UserService userService;
    private ArticleService articleService;

    private UserContactMapper userContactMapper;
    private UserMapper userMapper;
    private ArticleMapper articleMapper;

    public UsersResource(UserMapper userMapper, UserContactMapper userContactMapper, ArticleMapper articleMapper) {
        this.userMapper = userMapper;
        this.userContactMapper = userContactMapper;
        this.userService = new UserService(userMapper);
        this.articleMapper = articleMapper;
        this.articleService = new ArticleService(articleMapper);
    }

    public void supercedeUserServiceForForTestsOnly(UserService userService) {
        this.userService = userService;
    }

    /**
     * Type: GET
     * Route: /users/{userId}
     * Method used to see if a user exists
     *
     * @param phoneGeneratedId
     * @return
     * {
     *     true / false
     * }
     */
    @GET
    @Path("/users/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response exists(@PathParam("userId") String phoneGeneratedId) {
        Response response;
        boolean userExists = this.userService.doesUserExists(phoneGeneratedId);
        response = Response.status(Response.Status.OK).entity(userExists)
                               .build();
        System.out.println(response);
        return response;
    }


    /**
     * Type: PUT
     * Route: /users/{userId}
     * Method used to update the TCP information of a user. Example of
     * expected values:
     * {
     *     port: 1234,
     *     ip: "123.123.123.123",
     *     password: "querty",
     * }
     *
     * @param phoneGeneratedId
     * @param inputUser
     * @return
     * {
     *     phoneGeneratedId: "someId",
     *     port: 1234,
     *     ip: "123.123.123.123",
     *     statusId: 2,
     *     avatarId: 12
     * }
     */
    @PUT
    @Path("/users/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTCP(@PathParam("userId") String phoneGeneratedId,
                            User inputUser) {
        Response response;

        inputUser.setPassword(Integer.toString(inputUser.getPassword().hashCode()));

        inputUser.setPhoneGeneratedId(phoneGeneratedId);
        try {
            boolean isRequestValid = this.userService.validateUpdateTCPParams
                    (inputUser);
            if (!isRequestValid) {
                // You can't update a user if the request isn't valid
                // (some mandatory param aren't filled).
                response = Response.status(Response.Status.BAD_REQUEST).build();
                System.out.println(response);
                return response;
            }

            boolean userExists = this.userService.doesUserExists(phoneGeneratedId);
            if (!userExists) {
                // You can't update a user if it isn't created
                response = Response.status(Response.Status.NOT_FOUND).build();
                System.out.println(response);
                return response;
            }

            boolean isPasswordValid = this.userService.authenticate(inputUser);
            if (!isPasswordValid) {
                // Can't update the info to a user if you don't have the right password
                response = Response.status(Response.Status.UNAUTHORIZED).build();
                System.out.println(response);
                return response;
            }

            User userInMemory = this.userService.readUser(phoneGeneratedId);
            // Setting the parameters that can change into the object returned from memory
            userInMemory.setIp(inputUser.getIp());
            userInMemory.setPort(inputUser.getPort());

            // Launching the process of modifying the identity map and DB
            BriarUser returnObject = this.userService.modifyUser(userInMemory);

            // If no error is returned, we send back OK
            response = Response.status(Response.Status.OK).entity(returnObject).build();
            System.out.println(response);
            return response;

        } catch (Exception e) {
            // Any exception we catch is an internal server error.
            //TODO Eventually we'll need a log service.
            System.out.println(e);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            System.out.println(response);
            return response;
        }
    }

    /**
     * Type: GET
     * Route: /users/{userId}/profile
     * Method used to get the profile information of a user. Example of
     *
     * @param phoneGeneratedId
     * @return
     * {
     *     phoneGeneratedId: "someId",
     *     statusId: 2,
     *     avatarId: 12
     * }
     */
    @GET
    @Path("/users/{userId}/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(@PathParam("userId") String phoneGeneratedId) {
        Response response;

        try {

            boolean userExists = this.userService.doesUserExists(phoneGeneratedId);
            if (!userExists) {
                // You can't update a user if it isn't created
                response = Response.status(Response.Status.NOT_FOUND).build();
                System.out.println(response);
                return response;
            }

            User userInMemory = this.userService.readUser(phoneGeneratedId);

            BriarProfileUser returnObject = new BriarProfileUser(userInMemory);

            // If no error is returned, we send back OK
            response = Response.status(Response.Status.OK).entity(returnObject).build();
            System.out.println(response);
            return response;

        } catch (Exception e) {
            // Any exception we catch is an internal server error.
            //TODO Eventually we'll need a log service.
            System.out.println(e);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            System.out.println(response);
            return response;
        }
    }

    /**
     * Type: PUT
     * Route: /users/{userId}/profile
     * Method used to update the profile information of a user. Example of
     * expected values:
     * {
     *     statusId: 2,
     *     avatarId: 12,
     *     password: "querty"
     * }
     *
     * @param phoneGeneratedId
     * @param inputUser
     * @return
     * {
     *     phoneGeneratedId: "someId",
     *     port: 1234,
     *     ip: "123.123.123.123",
     *     statusId: 2,
     *     avatarId: 12
     * }
     */
    @PUT
    @Path("/users/{userId}/profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(@PathParam("userId") String phoneGeneratedId,
                              User inputUser) {
        Response response;

        inputUser.setPassword(Integer.toString(inputUser.getPassword().hashCode()));
        inputUser.setPhoneGeneratedId(phoneGeneratedId);
        try {
            boolean isRequestValid = this.userService
                    .validateUpdateProfileParams
                    (inputUser);
            if (!isRequestValid) {
                // You can't update a user if the request isn't valid
                // (some mandatory param aren't filled).
                response = Response.status(Response.Status.BAD_REQUEST).build();
                System.out.println(response);
                return response;
            }

            boolean userExists = this.userService.doesUserExists(phoneGeneratedId);
            if (!userExists) {
                // You can't update a user if it isn't created
                response = Response.status(Response.Status.NOT_FOUND).build();
                System.out.println(response);
                return response;
            }

            boolean isPasswordValid = this.userService.authenticate(inputUser);
            if (!isPasswordValid) {
                // Can't update the info to a user if you don't have the right password
                response = Response.status(Response.Status.UNAUTHORIZED).build();
                System.out.println(response);
                return response;
            }

            User userInMemory = this.userService.readUser(phoneGeneratedId);
            // Setting the parameters that can change into the object returned from memory
            userInMemory.setAvatarId(inputUser.getAvatarId());
            userInMemory.setStatusId(inputUser.getStatusId());

            // Launching the process of modifying the identity map and DB
            BriarUser returnObject = this.userService.modifyUser(userInMemory);

            // If no error is returned, we send back OK
            response = Response.status(Response.Status.OK).entity(returnObject).build();
            System.out.println(response);
            return response;

        } catch (Exception e) {
            // Any exception we catch is an internal server error.
            //TODO Eventually we'll need a log service.
            System.out.println(e);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            System.out.println(response);
            return response;
        }
    }

    /**
     * Type: POST
     * Route: /user
     * Method used to create the contact information of a user. Example of
     * expected values:
     * {
     *     port: 1234,
     *     ip: "123.123.123.123",
     *     phoneGeneratedId: "someId",
     *     password: "qwerty",
     *     statusId: 2,
     *     avatarId, 12
     * }
     * @param inputUser
     * @return
     * {
     *     phoneGeneratedId: "someId",
     *     port: 1234,
     *     ip: "123.123.123.123",
     *     statusId: 2,
     *     avatarId: 12
     * }
     */
    @POST
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User inputUser) {
        Response response;

        inputUser.setPassword(Integer.toString(inputUser.getPassword().hashCode()));

        boolean isRequestValid = this.userService.validateUserParams(inputUser);
        if (!isRequestValid) {
            // You can't update a user if the request isn't valid
            // (some mandatory param aren't filled).
            response = Response.status(Response.Status.BAD_REQUEST).build();
            System.out.println(response + " Request isn't valid");
            return response;
        }

        boolean userExists = this.userService.doesUserExists(inputUser.getPhoneGeneratedId());
        if (userExists) {
            // You can't create a user if it already exists
            response = Response.status(Response.Status.BAD_REQUEST).build();
            System.out.println(response + " User already exists");
            return response;
        }

        try {
            BriarUser returnValue = this.userService.addUser(inputUser);
            // Here everything worked and we return the user created
            response = Response.status(Response.Status.CREATED).entity(returnValue).build();
            System.out.println(response);
            return response;
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            System.out.println("ERROR: " + e + "\n\n\n" + e.getStackTrace() + "\n\n\n");
            System.out.println("RESPONSE: " + response);
            return response;
        }
    }

    @Path("/users/{userId}/contact")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ContactResource getContactResource() {
        return new ContactResource(userMapper, userContactMapper);
    }

    @Path("/users/{userId}/contacts")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ContactsResource getContactsResource() {
        return new ContactsResource(userMapper, userContactMapper);
    }

    @Path("/users/{userId}/article")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArticleResource getArticleResource() { return new ArticleResource(articleMapper, userMapper);}


}
