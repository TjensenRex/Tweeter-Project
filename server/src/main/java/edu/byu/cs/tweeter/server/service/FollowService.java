package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.*;
import edu.byu.cs.tweeter.model.net.response.*;
import edu.byu.cs.tweeter.server.dao.FakeFollowDAO;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {
    private final FollowDAO followDAO;
    FollowDAO getFollowDAO() {
        return followDAO;
    }
    public FollowService(FollowDAO followDAO) {
        this.followDAO = followDAO;
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FakeFollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit: " + request.getLimit());
        }

        Pair<List<User>, Boolean> pair = getFollowDAO().getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        return new FollowingResponse(pair.getFirst(), pair.getSecond());
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit: " + request.getLimit());
        }

        Pair<List<User>, Boolean> pair = getFollowDAO().getFollowers(request.getFolloweeAlias(), request.getLimit(), request.getLastFollowerAlias());
        return new FollowersResponse(pair.getFirst(), pair.getSecond());
    }
    public CountResponse getFollowingCount(CountRequest request) {
        return new CountResponse(getFollowDAO().getFolloweeCount(request.getUser()));
    }
    public CountResponse getFollowerCount(CountRequest request) {
        return new CountResponse(getFollowDAO().getFollowerCount(request.getUser()));
    }
    public FollowResponse setFollow(FollowRequest request) {
        return new FollowResponse(getFollowDAO().setFollow(request));
    }
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        try {
            //replace with a call to the DAOs
            boolean isFollower = getFollowDAO().isFollower(request.getFollowerAlias(), request.getFolloweeAlias());
            return new IsFollowerResponse(isFollower, true, "determined if they were a follower");
        } catch (Exception e) {
            return new IsFollowerResponse(false, e.getMessage());
        }
    }
}
