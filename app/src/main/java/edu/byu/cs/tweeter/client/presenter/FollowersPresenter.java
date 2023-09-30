package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.FollowersObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class FollowersPresenter {
    public interface View {
        void displayMessage(String message);
        void setLoadingFooter(boolean value);
        void startIntentActivity(User user);
        void addFollowers(List<User> followers);
    }
    private static final int PAGE_SIZE = 10;
    private User lastFollower;
    private boolean hasMorePages;
    public boolean hasMorePages() {
        return hasMorePages;
    }
    private boolean isLoading = false;
    public boolean isLoading() {
        return isLoading;
    }
    private View view;
    private UserService userService;
    private FollowService followService;
    public FollowersPresenter(View v) {
        view = v;
        userService = new UserService();
        followService = new FollowService();
    }
    public void getUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias,
                new UserServiceObserver());
    }
    public void getFollowers(User user) {
        isLoading = true;
        view.setLoadingFooter(true);
        followService.loadFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                lastFollower, new FollowServiceObserver());
    }
    private class UserServiceObserver implements UserObserver {
        @Override
        public void startActivity(User user) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.startIntentActivity(user);
        }
        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get user's profile: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }
    }

    private class FollowServiceObserver implements ListObserver<User> {
        @Override
        public void addItems(boolean hasMorePages, List<User> followers) {
            isLoading = false;
            view.setLoadingFooter(false);
            FollowersPresenter.this.hasMorePages = hasMorePages;
            FollowersPresenter.this.lastFollower = (followers.size() > 0) ?
                    followers.get(followers.size() - 1) : null;
            view.addFollowers(followers);
        }
        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get followers: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get followers because of exception: " + ex.getMessage());
        }
    }
}
