package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.AuthenticationPresenter;
import edu.byu.cs.tweeter.client.presenter.viewInterface.BaseView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthObserver implements ServiceObserver {
    private AuthenticationPresenter presenter;
    private String verb;
    public AuthObserver(AuthenticationPresenter p, String verb) {
        presenter = p;
        this.verb = verb;
    }
    public void startIntent(User registeredUser, AuthToken authToken) {
        presenter.startViewActivity(registeredUser, authToken);
    }
    @Override
    public void handleFailure(String message) {
        presenter.displayMessage("Failed to " + verb + ": " + message);
    }
    @Override
    public void handleException(Exception ex) {
        presenter.displayMessage("Failed to " + verb + " because of exception: " + ex.getMessage());
    }
}
