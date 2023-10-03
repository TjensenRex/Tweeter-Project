package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.BasePresenter;

public abstract class AuthenticatedObserver extends ServiceObserver {
    public AuthenticatedObserver(BasePresenter presenter, String verb) {
        super(presenter, verb);
    }

    public abstract void success();
}
