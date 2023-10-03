package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.*;
import edu.byu.cs.tweeter.client.presenter.viewInterface.BaseView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter extends BasePresenter {
    public interface View extends BaseView {
        void updateFollowButton(boolean value);
        void logoutSuccess();
        void setFollowerCount(int count);
        void setFollowingCount(int count);
        void setFollowButton(boolean value);
        void postSuccess();
    }
    private static final String LOG_TAG = "MainActivity";
    private FollowService followService;
    private UserService userService;
    private StatusService statusService;
    public MainActivityPresenter(View v) {
        super(v);
        followService = new FollowService();
        userService = new UserService();
        statusService = new StatusService();
    }
    @Override
    public View getView() {
        return (View) super.getView();
    }
    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowObserver());
    }
    public void logout() {
        userService.logout(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }
    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowerCountObserver(), new FollowingCountObserver());
    }
    public void follow(User selectedUser) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new AddFollowObserver());
        getView().displayMessage("Adding " + selectedUser.getName() + "...");
    }
    public void unfollow(User selectedUser) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new UnfollowObserver());
        getView().displayMessage("Removing " + selectedUser.getName() + "...");
    }
    public void post(String post) {
        List<String> urls = parseURLs(post);
        List<String> mentions = parseMentions(post);
        try{
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), System.currentTimeMillis(),
                    urls, mentions);

        statusService.post(Cache.getInstance().getCurrUserAuthToken(), newStatus, new PostObserver());
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            getView().displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }
    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }
        return containedMentions;
    }
    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }
    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);
                word = word.substring(0, index);
                containedUrls.add(word);
            }
        }

        return containedUrls;
    }
    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }
    public User getCurrUser() {
        return Cache.getInstance().getCurrUser();
    }
    public void clearCache() {
        Cache.getInstance().clearCache();
    }
    private class IsFollowObserver implements IsFollowerObserver {
        @Override
        public void updateFollowButton(boolean value) {
            getView().updateFollowButton(value);
        }
        @Override
        public void handleFailure(String message) {
            getView().displayMessage("Failed to determine following relationship: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            getView().displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }
    private class LogoutObserver implements AuthenticatedObserver {
        @Override
        public void success() {
            getView().logoutSuccess();
        }
        @Override
        public void handleFailure(String message) {
            getView().displayMessage("Failed to logout: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            getView().displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }
    }
    private class FollowerCountObserver implements GetCountObserver {
        @Override
        public void setCount(int count) {
            getView().setFollowerCount(count);
        }
        @Override
        public void handleFailure(String message) {
            getView().displayMessage("Failed to get followers count: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            getView().displayMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }
    }
    private class FollowingCountObserver implements GetCountObserver {
        @Override
        public void setCount(int count) {
            getView().setFollowingCount(count);
        }
        @Override
        public void handleFailure(String message) {
            getView().displayMessage("Failed to get following count: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            getView().displayMessage("Failed to get following count because of exception: " + ex.getMessage());
        }
    }
    private class AddFollowObserver implements ToggleFollowObserver {
        @Override
        public void success(boolean value, User selectedUser) {
            followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(),
                    selectedUser, new FollowerCountObserver(), new FollowingCountObserver());
            getView().updateFollowButton(false);
        }
        @Override
        public void handleFailure(String message) {
            getView().displayMessage("Failed to follow: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            getView().displayMessage("Failed to follow because of exception: " + ex.getMessage());
        }
        @Override
        public void setFollowButton(boolean value) {
            getView().setFollowButton(value);
        }
    }
    private class UnfollowObserver implements ToggleFollowObserver {
        @Override
        public void success(boolean value, User selectedUser) {
            followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(),
                    selectedUser, new FollowerCountObserver(), new FollowingCountObserver());
            getView().updateFollowButton(value);
        }
        @Override
        public void handleFailure(String message) {
            getView().displayMessage("Failed to unfollow: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            getView().displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }
        @Override
        public void setFollowButton(boolean value) {
            getView().setFollowButton(value);
        }
    }
    private class PostObserver implements AuthenticatedObserver {
        @Override
        public void success() {
            getView().postSuccess();
        }
        @Override
        public void handleFailure(String message) {
            getView().displayMessage("Failed to post status: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            getView().displayMessage("Failed to post status because of exception: " + ex.getMessage());
        }
    }
}
