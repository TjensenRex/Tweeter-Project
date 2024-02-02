package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.util.Pair;

import java.util.List;

public interface FollowDAO {
    Integer getFolloweeCount(User follower);

    Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias);

    int getUsersStartingIndex(String lastUserAlias, List<User> allUsers);

    Pair<List<User>, Boolean> getFollowers(String followeeAlias, int limit, String lastFollowerAlias);

    int getFollowerCount(User user);

    boolean setFollow(FollowRequest request);

    boolean isFollower(String followerAlias, String followeeAlias);

    void addFollowersBatch(List<String> followers, String followTarget);
}
