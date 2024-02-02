package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetListHandler;
import edu.byu.cs.tweeter.client.presenter.observer.ListObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {
    private final FollowService followService;
    public FollowersPresenter(PagedView<User> v) {
        super(v);
        followService = new FollowService();
    }
    @Override
    void callService(User user) {
        followService.loadMoreItems(new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE,
                getLastItem(), new GetListHandler<>(new ListObserver<>(this, "get followers"))));
    }
}
