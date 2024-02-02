package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class FollowRelationBean {
    private static final String IndexName = "follows-index";
    private String follower_handle;
    private String follower_firstName;
    private String follower_lastName;
    private String follower_image;
    private String followee_handle;
    private String followee_firstName;
    private String followee_lastName;
    private String followee_image;
    public FollowRelationBean() {}
    public FollowRelationBean(String fer_handle, String fee_handle) {
        follower_handle = fer_handle;
        followee_handle = fee_handle;
    }
    public FollowRelationBean(String fer_handle, User follower, String fee_handle, User followee) {
        follower_handle = fer_handle;
        follower_firstName = follower.getFirstName();
        follower_lastName = follower.getLastName();
        follower_image = follower.getImageUrl();

        followee_handle = fee_handle;
        followee_firstName = followee.getFirstName();
        followee_lastName = followee.getLastName();
        followee_image = followee.getImageUrl();
    }
    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = IndexName)
    public String getFollower_handle() {
        return follower_handle;
    }
    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }
    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = IndexName)
    public String getFollowee_handle() {
        return followee_handle;
    }
    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }
    public String getFollower_firstName() {
        return follower_firstName;
    }
    public void setFollower_firstName(String follower_firstName) {
        this.follower_firstName = follower_firstName;
    }
    public String getFollower_lastName() {
        return follower_lastName;
    }
    public void setFollower_lastName(String follower_lastName) {
        this.follower_lastName = follower_lastName;
    }
    public String getFollower_image() {
        return follower_image;
    }
    public void setFollower_image(String follower_image) {
        this.follower_image = follower_image;
    }
    public String getFollowee_firstName() {
        return followee_firstName;
    }
    public void setFollowee_firstName(String followee_firstName) {
        this.followee_firstName = followee_firstName;
    }
    public String getFollowee_lastName() {
        return followee_lastName;
    }
    public void setFollowee_lastName(String followee_lastName) {
        this.followee_lastName = followee_lastName;
    }
    public String getFollowee_image() {
        return followee_image;
    }
    public void setFollowee_image(String followee_image) {
        this.followee_image = followee_image;
    }
}
