package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;

import java.util.List;

public class ListObserver<T> implements ServiceObserver {
    private PagedPresenter<T> presenter;
    private String verb;
    public ListObserver(PagedPresenter<T> p, String t) {
        presenter = p;
        verb = t;
    }
    public void addItems(boolean hasMorePages, List<T> statuses) {
        presenter.addItems(hasMorePages, statuses);
    }
    @Override
    public void handleFailure(String message) {
        presenter.handleMessage("Failed to " + verb + ": " + message);
    }
    @Override
    public void handleException(Exception ex) {
        presenter.handleMessage("Failed to " + verb + " because of exception: " + ex.getMessage());
    }
}
