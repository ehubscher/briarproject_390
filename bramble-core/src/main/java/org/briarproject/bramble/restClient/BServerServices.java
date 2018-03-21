package org.briarproject.bramble.restClient;

import org.briarproject.bramble.restClient.ServerObj.SavedUser;

/**
 * Created by Winterhart on 3/10/2018.
 * Describe available method to for using Briar Server...
 */

public interface BServerServices {

    /**
     * This methis method is getting user info based on unique ID given
     * @param userID A unique String ID made of 20 random char.
     * @return The object class SavedUser
     */
    SavedUser obtainUserInfo(String userID);

    /**
     * Creating a new user in Briar Server
     * @param savedUser Info needed to create a new user
     * @return True -> Success, False -> Failure
     */
    boolean createNewUser(SavedUser savedUser);

    /**
     * Update an alrready saved user
     * @param savedUser All settings about the network
     * @return True -> Success, False -> Failure
     */
    boolean updateUserNetworkInfo(SavedUser savedUser);

    /**
     * Update the user preferences , avatar , status
     * @param savedUser All settings data to be updated
     * @return True -> Success, False -> Failure
     */
    boolean updateUserSettingInfo(SavedUser savedUser);

}