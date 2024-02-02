package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class IsFollowerRequest {
    private String followerAlias;
    private String followeeAlias;
    private AuthToken authToken;
    public IsFollowerRequest() {}
    public IsFollowerRequest(String follower, String followee, AuthToken authToken) {
        followerAlias = follower;
        followeeAlias = followee;
        this.authToken = authToken;
    }
    public String getFollowerAlias() {
        return followerAlias;
    }
    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }
    public String getFolloweeAlias() {
        return followeeAlias;
    }
    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
