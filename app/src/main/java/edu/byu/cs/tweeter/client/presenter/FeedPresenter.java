package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class FeedPresenter {
    public interface View {
        void displayMessage(String message);
        void startIntentActivity(User user);
        void setLoadingFooter(boolean value);
        void addStatuses(List<Status> statuses);
    }
    private static final int PAGE_SIZE = 10;
    private boolean isLoading = false;
    public boolean isLoading() {
        return isLoading;
    }
    private Status lastStatus;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    private boolean hasMorePages;
    private View view;
    private UserService userService;
    private StatusService feedService;
    public FeedPresenter(View view) {
        this.view = view;
        userService = new UserService();
        feedService = new StatusService();
    }
    public void getFeed(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            feedService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new StatusServiceObserver());
        }
    }
    public void getUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new UserServiceObserver());
        view.displayMessage("Getting user's profile...");
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
    private class StatusServiceObserver implements ListObserver<Status> {
        @Override
        public void addItems(boolean hasMorePages, List<Status> statuses) {
            isLoading = false;
            view.setLoadingFooter(false);
            FeedPresenter.this.hasMorePages = hasMorePages;
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addStatuses(statuses);
        }
        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }
    }
}
