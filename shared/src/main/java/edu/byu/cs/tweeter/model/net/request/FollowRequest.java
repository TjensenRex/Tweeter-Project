package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest {
    private User follower;
    private User followee;
    private Boolean isFollowing;
    private AuthToken authToken;
    public FollowRequest() {}
    public FollowRequest(User follower, User followee, AuthToken authToken, Boolean isFollowing) {
        this.follower = follower;
        this.followee = followee;
        this.authToken = authToken;
        this.isFollowing = isFollowing;
    }
    public User getFollower() {
        return follower;
    }
    public void setFollower(User follower) {
        this.follower = follower;
    }
    public User getFollowee() {
        return followee;
    }
    public void setFollowee(User followee) {
        this.followee = followee;
    }
    public Boolean getIsFollowing() {
        return isFollowing;
    }
    public void setIsFollowing(Boolean following) {
        isFollowing = following;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
