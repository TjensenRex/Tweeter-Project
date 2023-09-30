package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class FollowingPresenter {
    public interface View {
        void setLoadingFooter(boolean value);
        void displayMessage(String message);
        void addMoreFollowees(List<User> followees);

        void startIntentActivity(User user);
    }
    private User lastFollowee;
    private boolean isLoading = false;
    public boolean isLoading() {
        return isLoading;
    }
    private boolean hasMorePages;
    public boolean hasMorePages() {
        return hasMorePages;
    }
    private static final int PAGE_SIZE = 10;
    private View view;
    private FollowService followService;
    private UserService userService;
    public FollowingPresenter(View v) {
        this.view = v;
        this.followService = new FollowService();
        this.userService = new UserService();
    }
    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followService.loadMoreItems(Cache.getInstance().getCurrUserAuthToken(),
                    user, PAGE_SIZE, lastFollowee, new FollowServiceObserver());
        }
    }

    public void getUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new UserServiceObserver());
        view.displayMessage("Getting user's profile...");
    }


    private class FollowServiceObserver implements ListObserver<User> {
        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
        }

        @Override
        public void addItems(boolean hasMorePages, List<User> followees) {
            isLoading = false;
            view.setLoadingFooter(false);
            FollowingPresenter.this.hasMorePages = hasMorePages;
            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            view.addMoreFollowees(followees);
        }
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
}
