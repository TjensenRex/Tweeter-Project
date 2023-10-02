package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.*;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.*;
import edu.byu.cs.tweeter.client.model.service.observer.*;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FollowService extends BaseService {
    public void unfollow(AuthToken currUserAuthToken, User selectedUser, ToggleFollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken, selectedUser,
                new UnfollowHandler(observer, selectedUser));
        executeTask(unfollowTask);
    }
    public void loadMoreItems(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee,
                              ListObserver<User> observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken, user, pageSize, lastFollowee,
                new GetListHandler<User>(observer));
        executeTask(getFollowingTask);
    }
    public void loadFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower,
                              ListObserver<User> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken, user, pageSize, lastFollower,
                new GetListHandler<User>(observer));
        executeTask(getFollowersTask);
    }
    public void isFollower(AuthToken currUserAuthToken, User currUser, User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken, currUser, selectedUser,
                new IsFollowerHandler(observer));
        executeTask(isFollowerTask);
    }
    public void updateSelectedUserFollowingAndFollowers(AuthToken currUserAuthToken, User selectedUser,
                                                        GetCountObserver observer1, GetCountObserver observer2) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetCountHandler(observer1));
        executor.execute(followersCountTask);
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetCountHandler(observer2));
        executor.execute(followingCountTask);
    }
    public void follow(AuthToken currUserAuthToken, User selectedUser, ToggleFollowObserver observer) {
        FollowTask followTask = new FollowTask(currUserAuthToken, selectedUser,
                new FollowHandler(observer, selectedUser));
        executeTask(followTask);
    }
}
