package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.client.presenter.viewInterface.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class UserObserver implements ServiceObserver {
    private PagedView view;
    private PagedPresenter presenter;
    public UserObserver(PagedView v, PagedPresenter p) {
        view = v;
        presenter = p;
    }
    public void startActivity(User user) {
        presenter.setLoading(false);
        view.setLoadingFooter(false);
        view.startActivity(user);
    }
    @Override
    public void handleFailure(String message) {
        presenter.setLoading(false);
        view.setLoadingFooter(false);
        view.displayMessage("Failed to get user's profile: " + message);
    }
    @Override
    public void handleException(Exception ex) {
        presenter.setLoading(false);
        view.setLoadingFooter(false);
        view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
    }
}
