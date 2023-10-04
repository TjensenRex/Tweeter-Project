package edu.byu.cs.tweeter.client.presenter.observer;

import edu.byu.cs.tweeter.client.presenter.PagedPresenter;

import java.util.List;

public class ListObserver<T> extends ServiceObserver {
    public ListObserver(PagedPresenter<T> p, String t) {
        super(p, t);
    }
    public void addItems(boolean hasMorePages, List<T> statuses) {
        getPresenter().addItems(hasMorePages, statuses);
    }
    @Override
    public PagedPresenter<T> getPresenter() {
        return (PagedPresenter<T>) super.getPresenter();
    }
}
