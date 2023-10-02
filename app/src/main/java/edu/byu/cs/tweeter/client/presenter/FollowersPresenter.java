package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {
    private FollowService followService;
    public FollowersPresenter(PagedView<User> v) {
        super(v);
        followService = new FollowService();
    }
    @Override
    void callService(User user) {
        followService.loadFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                getLastItem(), new ListObserver<User>(view, this, "followers"));
    }
}
