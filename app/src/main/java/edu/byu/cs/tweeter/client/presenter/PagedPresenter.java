package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

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
        getUserService().getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new UserObserver(getView(), this));
        getView().displayMessage("Getting user's profile...");
    }
    public void loadMoreItems(User user) {
        if (!isLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            getView().setLoadingFooter(true);
            callService(user);
        }
    }
    abstract void callService(User user);
}
