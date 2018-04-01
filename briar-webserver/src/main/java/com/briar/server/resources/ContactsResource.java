package com.briar.server.resources;

import com.briar.server.mapper.UserContactMapper;
import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.User;
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
     * Method used to
     * @param userId
     * @param targetId
     * @param userParam
     * @return
     */
    @POST
    @Path("/{targetId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateContact(@PathParam("userId") String userId, @PathParam("targetId") String targetId,
                                    User userParam) {
        Response response = null;
        User userToAuthentify = new User();
        userToAuthentify.setPassword(userParam.getPassword());
        userToAuthentify.setPhoneGeneratedId(userId);
        return response;




    }

}
