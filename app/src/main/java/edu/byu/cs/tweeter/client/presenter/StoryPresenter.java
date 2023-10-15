package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.observer.ListObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {
    private StatusService statusService;
    public StoryPresenter(PagedView<Status> view) {
        super(view);
        statusService = getStatusService();
    }
    private StatusService getStatusService() {
        return new StatusService();
    }
    @Override
    void callService(User user) {
        statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, getLastItem(),
                new ListObserver<Status>(this, "get story"));
    }
}
