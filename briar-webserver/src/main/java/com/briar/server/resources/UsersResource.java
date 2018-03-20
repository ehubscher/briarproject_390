package com.briar.server.resources;

import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.returnedtobriarclasses.BriarUser;
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

    private UserMapper userMapper;

    public UsersResource(UserMapper userMapper) {
        this.userMapper = userMapper;
        this.userService = new UserService(userMapper);
    }

    public void supercedeUserServiceForForTestsOnly(UserService userService) {
        this.userService = userService;
    }

    /**
     * Method used to update the contact information of a user. Expected values
     * {
     * port: 1234,
     * ip: "123.123.123.123",
     * password: "querty"
     * }
     *
     * @param phoneGeneratedId
     * @param inputUser
     * @return
     */
    @PUT
    @Path("/users/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") String phoneGeneratedId,
                               User inputUser) {
        Response response;

        inputUser.setPhoneGeneratedId(phoneGeneratedId);
        try {
            boolean isRequestValid =
                    this.userService.validateUserParams(inputUser);
            if (!isRequestValid) {
                // You can't update a user if the request isn't valid
                // (some mandatory param aren't filled).
                response = Response.status(Response.Status.BAD_REQUEST).build();
                System.out.println(response);
                return response;
            }

            boolean userExists =
                    this.userService.doesUserExists(phoneGeneratedId);
            if (!userExists) {
                // You can't update a user if it isn't created
                response = Response.status(Response.Status.NOT_FOUND).build();
                System.out.println(response);
                return response;
            }

            boolean isPasswordValid = this.userService.authenticate(inputUser);
            if (!isPasswordValid) {
                // Can't update the info to a user if you don't have the right password
                response =
                        Response.status(Response.Status.UNAUTHORIZED).build();
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
            response = Response.status(Response.Status.OK).entity(returnObject)
                               .build();
            System.out.println(response);
            return response;

        } catch (Exception e) {
            // Any exception we catch is an internal server error.
            //TODO Eventually we'll need a log service.
            System.out.println(e);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                               .build();
            System.out.println(response);
            return response;
        }
    }

    /**
     * Method used to create the contact information of a user. Input required:
     * {
     * port: 1234,
     * ip: "123.123.123.123",
     * phoneGeneratedId: "someId",
     * password: "qwerty"
     * }
     *
     * @param inputUser
     * @return
     */
    @POST
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User inputUser) {
        Response response;

        boolean isRequestValid = this.userService.validateUserParams(inputUser);
        if (!isRequestValid) {
            // You can't update a user if the request isn't valid
            // (some mandatory param aren't filled).
            response = Response.status(Response.Status.BAD_REQUEST).build();
            System.out.println(response + " Request isn't valid");
            return response;
        }

        boolean userExists = this.userService
                .doesUserExists(inputUser.getPhoneGeneratedId());
        if (userExists) {
            // You can't create a user if it already exists
            response = Response.status(Response.Status.BAD_REQUEST).build();
            System.out.println(response + " User already exists");
            return response;
        }

        try {
            BriarUser returnValue = this.userService.addUser(inputUser);
            // Here everything worked and we return the user created
            response =
                    Response.status(Response.Status.CREATED).entity(returnValue)
                            .build();
            System.out.println(response);
            return response;
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                               .build();
            System.out.println(
                    "ERROR: " + e + "\n\n\n" + e.getStackTrace() + "\n\n\n");
            System.out.println("RESPONSE: " + response);
            return response;
        }
    }

    /**
     * Temporary access point to get the user information. Will be deleted in order to make place to a user contact
     * system instead. Expects:
     * {
     * "password": "somePassword"
     * }
     *
     * @param phoneGeneratedId
     * @param inputUser
     * @return
     */
    @POST
    @Path("/hack/users/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response returnUser(@PathParam("userId") String phoneGeneratedId,
                               User inputUser) {
        Response response;
        inputUser.setPhoneGeneratedId(phoneGeneratedId);

        boolean isRequestValid =
                this.userService.validateHackUserParams(inputUser);
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
            System.out.println(response + " User already exists");
            return response;
        }

        try {
            User returnUser = this.userService.readUser(phoneGeneratedId);
            BriarUser returnValue =
                    this.userService.convertUserToBriarUser(returnUser);
            // Here everything worked and we return the user created
            response = Response.status(Response.Status.OK).entity(returnValue)
                               .build();
            System.out.println(response);
            return response;
        } catch (Exception e) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                               .build();
            System.out.println(
                    "ERROR: " + e + "\n\n\n" + e.getStackTrace() + "\n\n\n");
            System.out.println("RESPONSE: " + response);
            return response;
        }
    }
}
