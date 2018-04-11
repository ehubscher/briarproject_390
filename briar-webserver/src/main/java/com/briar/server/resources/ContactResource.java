package com.briar.server.resources;

import com.briar.server.mapper.UserContactMapper;
import com.briar.server.mapper.UserMapper;
import com.briar.server.model.domainmodelclasses.User;
import com.briar.server.model.domainmodelclasses.UserContact;
import com.briar.server.model.request.AddContactRequest;
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
public class ContactResource {

    private UserMapper userMapper;
    private UserContactMapper userContactMapper;
    private UserService userService;
    private UserContactService userContactService;

    public ContactResource(UserMapper userMapper, UserContactMapper userContactMapper) {
        this.userMapper = userMapper;
        this.userContactMapper = userContactMapper;
        this.userService = new UserService(userMapper);
        this.userContactService = new UserContactService(userContactMapper, userMapper);
    }

    /**
     * POST
     * Route: /users/{userId}/contact
     * Payload:
     * {
     *     "password": "qwerty",
     *     "targetPhoneGeneratedId": "HelloBob"
     * }
     * return value: 200 OK
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateContact(@PathParam("userId") String userId, AddContactRequest contactRequest) {

        Response response;
        contactRequest.setPassword((Integer.toString(contactRequest.getPassword().hashCode())));

        boolean isRequestValid = this.userContactService.validateContactRequest(contactRequest);
        if (!isRequestValid) {
            // You can't perform any operation if the required information aren't there.
            response = Response.status(Response.Status.BAD_REQUEST).build();
            System.out.println(response);
            return response;
        }

        String targetId = contactRequest.getTargetPhoneGeneratedId();
        boolean userExists = this.userService.doesUserExists(userId);
        boolean targetExists = this.userService.doesUserExists(targetId);
        if (!userExists || !targetExists) {
            // You can't create the contact if either user doesn't exist
            response = Response.status(Response.Status.NOT_FOUND).build();
            System.out.println(response);
            return response;
        }

        try {

            // Here we try to authenticate the user
            User userToAuthenticate = new User();
            userToAuthenticate.setPhoneGeneratedId(userId);
            userToAuthenticate.setPassword(contactRequest.getPassword());
            boolean isPasswordValid = this.userService.authenticate(userToAuthenticate);
            if (!isPasswordValid) {
                // Can't update the info to a user if you don't have the right password
                response = Response.status(Response.Status.UNAUTHORIZED).build();
                System.out.println(response);
                return response;
            }


            boolean userContactExists = this.userContactService.doesUserContactExists(userId, targetId);
            if (userContactExists) {
                UserContact existingUserContact = this.userContactService.readUserContact(userId, targetId);

                // We do this test to know which side of the bilateral contact acceptation we need to modify
                boolean acceptContact = true;
                if (targetId.compareTo(userId) <= 0) {
                    existingUserContact.setSecondUserContactAcceptance(acceptContact);
                } else {
                    existingUserContact.setFirstUserContactAcceptance(acceptContact);
                }

                // Then we post this user contact to the db and identity map
                this.userContactService.modifyUserContact(existingUserContact);
                return Response.status(Response.Status.CREATED).build();
            } else {
                // We fetch both users so that we can create the UserContact.
                User initiatingUser = this.userService.readUser(userId);
                User targetUser = this.userService.readUser(targetId);

                // We create the user contact
                UserContact userContact = createNewUserContact(initiatingUser, targetUser);

                // We put it into the DB and the IdentityMap
                this.userContactService.addUserContact(userContact);
                return Response.status(Response.Status.CREATED).build();
            }
        } catch (Exception e) {
            System.out.println(e);
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            System.out.println(response);
            return response;
        }
    }

    private UserContact createNewUserContact(User contactInitiater, User contactTarget) {

        String initiaterId = contactInitiater.getPhoneGeneratedId();
        String targetId = contactTarget.getPhoneGeneratedId();

        // We use an internal object to package the info necessary for the usercontact
        boolean acceptsTheContactOffer = true;
        UnilateralUserContact unilateralInitiater = new UnilateralUserContact(contactInitiater, acceptsTheContactOffer);
        acceptsTheContactOffer = false;
        UnilateralUserContact unilateralTarget = new UnilateralUserContact(contactTarget, acceptsTheContactOffer);


        // We sort the contacts in lexicographic order so that the smaller user id is always entered first in the
        // contacts
        UnilateralUserContact firstContact = unilateralInitiater;
        UnilateralUserContact secondContact = unilateralTarget;
        if (initiaterId.compareTo(targetId) > 0) {
            firstContact = unilateralTarget;
            secondContact = unilateralInitiater;
        }

        UserContact returnObject = new UserContact();
        returnObject.setFirstUserContactAcceptance(firstContact.isContactAccepted());
        returnObject.setFirstUserId(firstContact.getId());
        returnObject.setFirstUserName(firstContact.getUserName());

        returnObject.setSecondUserContactAcceptance(secondContact.isContactAccepted());
        returnObject.setSecondUserId(secondContact.getId());
        returnObject.setSecondUserName(secondContact.getUserName());

        return returnObject;


    }

    private class UnilateralUserContact {
        private String userName;
        private long id;
        private boolean contactAccepted;

        public UnilateralUserContact(User user, boolean contactAccepted) {
            this.userName = user.getPhoneGeneratedId();
            this.id = user.getId();
            this.contactAccepted = contactAccepted;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public boolean isContactAccepted() {
            return contactAccepted;
        }

        public void setContactAccepted(boolean contactAcceptance) {
            this.contactAccepted = contactAcceptance;
        }
    }

}
