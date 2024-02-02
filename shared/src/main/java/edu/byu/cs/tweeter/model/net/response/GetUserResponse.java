package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class GetUserResponse extends Response {
    private User user;
    public GetUserResponse(boolean success, User user) {
        super(success);
        this.user = user;
    }
    public GetUserResponse(boolean success) {
        super(success);
    }
    public GetUserResponse(boolean success, String message) {
        super(success, message);
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
