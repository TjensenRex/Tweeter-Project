package edu.byu.cs.tweeter.client.presenter.observer;

import edu.byu.cs.tweeter.client.presenter.BasePresenter;

public class ServiceObserver {
    private final BasePresenter presenter;
    protected BasePresenter getPresenter() {
        return presenter;
    }

    public String getVerb() {
        return verb;
    }

    private final String verb;
    public ServiceObserver(BasePresenter presenter, String verb) {
        this.presenter = presenter;
        this.verb = verb;
    }
    public void handleFailure(String message) {
        presenter.displayMessage("Failed to " + verb + ": " + message);
    }
    public void handleException(Exception ex) {
        presenter.displayMessage("Failed to " + verb + " because of exception: " + ex.getMessage());
    }
}
