package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class CountRequest {
    public User getUser() {
        return user;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    private User user;
    private AuthToken authToken;
    public CountRequest(User targetUser, AuthToken authToken) {
        user = targetUser;
        this.authToken = authToken;
    }
    public CountRequest() {}
}
