package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.observer.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.presenter.observer.GetCountObserver;
import edu.byu.cs.tweeter.client.presenter.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.presenter.observer.ToggleFollowObserver;
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
    public interface MainView extends BaseView {
        void updateFollowButton(boolean value);
        void logoutSuccess();
        void setFollowerCount(int count);
        void setFollowingCount(int count);
        void setFollowButton(boolean value);
        void showPostToast();
        void postSuccess();
    }
    private static final String LOG_TAG = "MainActivity";
    private final FollowService followService;
    private final UserService userService;
    private StatusService statusService;
    public MainActivityPresenter(MainView v) {
        super(v);
        followService = new FollowService();
        userService = new UserService();
    }
    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }
    @Override
    public MainView getView() {
        return (MainView) super.getView();
    }
    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser,
                new IsFollowObserver(this, "determine following relationship"));
    }
    public void logout() {
        userService.logout(Cache.getInstance().getCurrUserAuthToken(),
                new LogoutObserver(this, "logout"));
    }
    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowerCountObserver(this, "get followers count"),
                new FollowingCountObserver(this,"get following count"));
    }
    public void follow(User selectedUser) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                new UpdateFollowStatusObserver(this, "follow"));
        getView().displayMessage("Adding " + selectedUser.getName() + "...");
    }
    public void unfollow(User selectedUser) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser,
                new UpdateFollowStatusObserver(this, "unfollow"));
        getView().displayMessage("Removing " + selectedUser.getName() + "...");
    }
    public void post(String post) {
        getView().showPostToast();
        List<String> urls = parseURLs(post);
        List<String> mentions = parseMentions(post);
        //try{
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), System.currentTimeMillis(),
                    urls, mentions);

        getStatusService().post(Cache.getInstance().getCurrUserAuthToken(), newStatus,
                new PostObserver(this, "post status"));
        //} catch (Exception ex) {
        //    Log.e(LOG_TAG, ex.getMessage(), ex);
          //  getView().displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        //}
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
    private class IsFollowObserver extends IsFollowerObserver {
        public IsFollowObserver(BasePresenter presenter, String verb) {
            super(presenter, verb);
        }
        @Override
        public void updateFollowButton(boolean value) {
            getView().updateFollowButton(value);
        }
    }
    private class LogoutObserver extends AuthenticatedObserver {
        public LogoutObserver(BasePresenter presenter, String verb) {
            super(presenter, verb);
        }
        @Override
        public void success() {
            getView().logoutSuccess();
        }
    }
    private class FollowerCountObserver extends GetCountObserver {
        public FollowerCountObserver(BasePresenter presenter, String verb) {
            super(presenter, verb);
        }
        @Override
        public void setCount(int count) {
            getView().setFollowerCount(count);
        }
    }
    private class FollowingCountObserver extends GetCountObserver {
        public FollowingCountObserver(BasePresenter presenter, String verb) {
            super(presenter, verb);
        }
        @Override
        public void setCount(int count) {
            getView().setFollowingCount(count);
        }
    }
    private class UpdateFollowStatusObserver extends ToggleFollowObserver {
        public UpdateFollowStatusObserver(BasePresenter presenter, String verb) {
            super(presenter, verb);
        }
        @Override
        public void success(boolean value, User selectedUser) {
            followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(),
                    selectedUser,
                    new FollowerCountObserver(MainActivityPresenter.this, "get followers count"),
                    new FollowingCountObserver(MainActivityPresenter.this, "get following count"));
            getView().updateFollowButton(value);
        }
        @Override
        public void setFollowButton(boolean value) {
            getView().setFollowButton(value);
        }
    }
    class PostObserver extends AuthenticatedObserver {
        public PostObserver(BasePresenter presenter, String verb) {
            super(presenter, verb);
        }
        @Override
        public void success() {
            getView().postSuccess();
        }
    }
}
