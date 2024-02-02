package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.*;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.*;
import edu.byu.cs.tweeter.client.presenter.observer.GetCountObserver;
import edu.byu.cs.tweeter.client.presenter.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.presenter.observer.ToggleFollowObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends BaseService {

    public static final String FOLLOWING_URL_PATH = "/getfollowing";
    public static final String FOLLOWERS_URL_PATH = "/getfollowers";
    public static final String FOLLOWING_COUNT_URL = "/getfollowingcount";
    public static final String FOLLOWER_COUNT_URL = "/getfollowercount";
    public static final String SET_FOLLOW_URL = "/setfollow";
    public static final String IS_FOLLOWER_URL = "/isfollower";

    /**
     * An observer interface to be implemented by observers who want to be notified when
     * asynchronous operations complete.
     */
    public interface GetFollowingObserver {
        void handleSuccess(List<User> followees, boolean hasMorePages);
        void handleFailure(String message);
        void handleException(Exception exception);
    }
    public void unfollow(AuthToken currUserAuthToken, User selectedUser, ToggleFollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken, selectedUser,
                new UnfollowHandler(observer, selectedUser));
        executeTask(unfollowTask);
    }

    /*
     * Requests the users that the user specified in the request is following.
     * Limits the number of followees returned and returns the next set of
     * followees after any that were returned in a previous request.
     * This is an asynchronous operation.
     *
     * @param authToken the session auth token.
     * @param targetUser the user for whom followees are being retrieved.
     * @param limit the maximum number of followees to return.
     * @param lastFollowee the last followee returned in the previous request (can be null).
     */
    /*public void getFollowees(AuthToken authToken, User targetUser, int limit, User lastFollowee, GetFollowingObserver observer) {
        GetFollowingTask followingTask = getGetFollowingTask(authToken, targetUser, limit, lastFollowee, observer);
        BackgroundTaskUtils.runTask(followingTask);
    }*/
    public void loadMoreItems(BackgroundTask task) {
        executeTask(task);
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
        FollowTask followTask = new FollowTask(currUserAuthToken, selectedUser, /*Cache.getInstance().getCurrUser().getAlias(),*/
                new FollowHandler(observer, selectedUser));
        executeTask(followTask);
    }
    /**
     * Returns an instance of {@link GetFollowingTask}. Allows mocking of the
     * GetFollowingTask class for testing purposes. All usages of GetFollowingTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public GetFollowingTask getGetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee, GetFollowingObserver observer) {
        return new GetFollowingTask(authToken, targetUser, limit, lastFollowee, new GetFollowingTaskHandler(observer));
    }
}
