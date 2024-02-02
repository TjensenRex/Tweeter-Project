package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends AuthenticateTask {
    private static final String LOG_TAG = "RegisterTask";
    /**
     * The user's first name.
     */
    private final String firstName;
    
    /**
     * The user's last name.
     */
    private final String lastName;

    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private final String image;
    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(messageHandler, username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }
    @Override
    protected void runTask() {
        try {
            RegisterRequest request = new RegisterRequest(username, password, firstName, lastName, image);
            RegisterResponse response = getServerFacade().register(request, UserService.REGISTER_URL);

            if (response.isSuccess()) {
                User user = response.getUser();
                AuthToken authToken = response.getAuthToken();
                Cache.getInstance().setCurrUser(user);
                Cache.getInstance().setCurrUserAuthToken(authToken);
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }
    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {
        User registeredUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(registeredUser, authToken);
    }
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, Cache.getInstance().getCurrUser());
        msgBundle.putSerializable(AUTH_TOKEN_KEY, Cache.getInstance().getCurrUserAuthToken());
    }
}
