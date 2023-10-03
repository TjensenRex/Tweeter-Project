package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {
    private FollowService followService;
    public FollowingPresenter(PagedView v) {
        super(v);
        this.followService = new FollowService();
    }
    @Override
    void callService(User user) {
        followService.loadMoreItems(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, getLastItem(), new ListObserver(getView(), this, "following"));
    }
}
