package com.briar.server.resources;

import com.briar.server.mapper.UserContactMapper;
import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.returnedtobriarclasses.BriarUser;
import com.briar.server.services.UserContactService;
import com.briar.server.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/")
@Api
public class ContactsResource {
    private UserMapper userMapper;
    private UserContactMapper userContactMapper;
    private UserService userService;
    private UserContactService userContactService;

    public ContactsResource(UserMapper userMapper, UserContactMapper userContactMapper) {
        this.userMapper = userMapper;
        this.userContactMapper = userContactMapper;
        this.userService = new UserService(userMapper);
        this.userContactService = new UserContactService(userContactMapper, userMapper);
    }


    /**
     * Type: POST
     * Route: /users/{userId}/contacts/{targetId}
     * Temporary access point to get the user information. Will be deleted in order to make place to a user contact
     * system instead. Expects:
     * {
     *     "password": "somePassword"
     * }
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
    @POST
    @Path("/{targetId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response retrieveContact(@PathParam("userId") String phoneGeneratedId, @PathParam("targetId") String targetPhoneGeneratedId,
                                    User inputUser) {
        Response response;
        inputUser.setPhoneGeneratedId(phoneGeneratedId);

        boolean isRequestValid = this.userService.validateHackUserParams(inputUser);
        if (!isRequestValid) {
            // You can't update a user if the request isn't valid
            // (some mandatory param aren't filled).
            response = Response.status(Response.Status.BAD_REQUEST).build();
            System.out.println(response + " Request isn't valid");
            return response;
        }

        boolean userExists = this.userService.doesUserExists(phoneGeneratedId);
        if (!userExists) {
            // You can't create a user if it already exists
            response = Response.status(Response.Status.BAD_REQUEST).build();
            System.out.println(response + " User doesn't exist");
            return response;
        }

        boolean isTargetUserInBilateralWithCurrentUser = this.userContactService.doesUserContactExists(phoneGeneratedId, targetPhoneGeneratedId);
        if(!isTargetUserInBilateralWithCurrentUser){
            // You can't return a contact if there is no bilateral connection
            response = Response.status(Response.Status.UNAUTHORIZED).build();
            System.out.println(response + " No Bilateral Connection");
            return response;
        }

        try {
            User returnUser = this.userService.readUser(targetPhoneGeneratedId);
            BriarUser returnValue = this.userService.convertUserToBriarUser(returnUser);
            // Here everything worked and we return the user created
            response = Response.status(Response.Status.OK).entity(returnValue).build();
            System.out.println(response);
            return response;
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            System.out.println("ERROR: " + e + "\n\n\n" + e.getStackTrace() + "\n\n\n");
            System.out.println("RESPONSE: " + response);
            return response;
        }
    }
}
