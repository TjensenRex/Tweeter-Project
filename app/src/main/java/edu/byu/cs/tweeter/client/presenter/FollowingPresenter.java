package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetListHandler;
import edu.byu.cs.tweeter.client.presenter.observer.ListObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {
    private FollowService followService;
    public FollowingPresenter(PagedView v) {
        super(v);
        this.followService = new FollowService();
    }
    @Override
    void callService(User user) {
        AuthToken currUserAuthToken = Cache.getInstance().getCurrUserAuthToken();
        followService.loadMoreItems(new GetFollowingTask(currUserAuthToken, user, PAGE_SIZE, getLastItem(),
                        new GetListHandler<User>(new ListObserver(this, "get following"))));
    }
}
