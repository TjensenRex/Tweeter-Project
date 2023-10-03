package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.BasePresenter;

public class ServiceObserver {
    private BasePresenter presenter;
    public BasePresenter getPresenter() {
        return presenter;
    }

    public String getVerb() {
        return verb;
    }

    private String verb;
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
