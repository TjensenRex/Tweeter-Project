package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public abstract class PagedPresenter<T> extends BasePresenter {
    protected static final int PAGE_SIZE = 10;
    private T lastItem;
    public T getLastItem() {
        return lastItem;
    }
    public void setLastItem(T lastItem) {
        this.lastItem = lastItem;
    }
    public void setLoading(boolean loading) {
        isLoading = loading;
    }
    private boolean isLoading = false;
    public boolean isLoading() {
        return isLoading;
    }
    private boolean hasMorePages;
    public boolean hasMorePages() {
        return hasMorePages;
    }
    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    private UserService userService;
    public UserService getUserService() {
        return userService;
    }
    public PagedPresenter(PagedView pagedView) {
        super(pagedView);
        userService = new UserService();
    }
    public PagedView<T> getView() {
        return (PagedView<T>) super.getView();
    }
    public void getUser(String userAlias) {
        getUserService().getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias,
                new UserObserver(this, "get user's profile"));
        getView().displayMessage("Getting user's profile...");
    }
    public void loadMoreItems(User user) {
        if (!isLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            getView().setLoadingFooter(true);
            callService(user);
        }
    }
    public void addItems(boolean value, List<T> statuses) {
        setLoading(false);
        getView().setLoadingFooter(false);
        setHasMorePages(value);
        setLastItem((statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null);
        getView().addItems(statuses);
    }
    public void handleMessage(String message) {
        setLoading(false);
        getView().setLoadingFooter(false);
        getView().displayMessage(message);
    }
    public void startActivity(User user) {
        setLoading(false);
        getView().setLoadingFooter(false);
        getView().startActivity(user);
    }
    abstract void callService(User user);
}
