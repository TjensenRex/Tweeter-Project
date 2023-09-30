package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class StoryPresenter {
    public interface View {
        void displayMessage(String s);
        void startActivity(User user);
        void setLoadingFooter(boolean value);
        void addItems(List<Status> statuses);
    }
    private View view;
    private UserService userService;
    private StatusService statusService;
    private Status lastStatus;
    private boolean hasMorePages;
    public boolean hasMorePages() {
        return hasMorePages;
    }
    private boolean isLoading = false;
    public boolean isLoading() {
        return isLoading;
    }
    private static final int PAGE_SIZE = 10;
    public StoryPresenter(View view) {
        this.view = view;
        userService = new UserService();
        statusService = new StatusService();
    }
    public void getUser(String alias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), alias, new UserObserver());
        view.displayMessage("Getting user's profile...");
    }
    public void loadStory(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus,
                    new StoryObserver());
        }
    }
    private class UserObserver implements edu.byu.cs.tweeter.client.model.service.observer.UserObserver {
        @Override
        public void startActivity(User user) {
            view.startActivity(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }
    }
    private class StoryObserver implements ListObserver<Status> {
        @Override
        public void addItems(boolean hasMorePages, List<Status> statuses) {
            StoryPresenter.this.hasMorePages = hasMorePages;
            isLoading = false;
            view.setLoadingFooter(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            view.addItems(statuses);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get story: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get story because of exception: " + ex.getMessage());
        }
    }
}
