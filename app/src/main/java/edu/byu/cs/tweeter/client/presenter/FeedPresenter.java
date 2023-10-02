package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {
    private StatusService feedService;
    public FeedPresenter(PagedView<Status> view) {
        super(view);
        feedService = new StatusService();
    }
    @Override
    void callService(User user) {
        feedService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, getLastItem(),
                new ListObserver(view, this, "feed"));
    }
}
