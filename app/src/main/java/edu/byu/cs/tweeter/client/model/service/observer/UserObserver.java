package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class UserObserver<T> implements ServiceObserver {
    private PagedPresenter<T> presenter;
    private String verb;
    public UserObserver(PagedPresenter<T> p, String verb) {
        presenter = p;
        this.verb = verb;
    }
    public void startActivity(User user) {
        presenter.startActivity(user);
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
