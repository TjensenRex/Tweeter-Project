package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;

import java.util.List;

public class ListObserver<T> implements ServiceObserver {
    private PagedView<T> view;
    private PagedPresenter presenter;
    private String token;
    public ListObserver(PagedView<T> pv, PagedPresenter<T> p, String t) {
        view = pv;
        presenter = p;
        token = t;
    }
    public void addItems(boolean hasMorePages, List<T> statuses) {
        presenter.setLoading(false);
        view.setLoadingFooter(false);
        presenter.setHasMorePages(hasMorePages);
        presenter.setLastItem((statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null);
        view.addItems(statuses);
    }
    @Override
    public void handleFailure(String message) {
        presenter.setLoading(false);
        view.setLoadingFooter(false);
        view.displayMessage("Failed to get " + token + ": " + message);
    }
    @Override
    public void handleException(Exception ex) {
        presenter.setLoading(false);
        view.setLoadingFooter(false);
        view.displayMessage("Failed to get " + token + " because of exception: " + ex.getMessage());
    }
}
