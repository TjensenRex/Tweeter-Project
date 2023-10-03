package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.presenter.BasePresenter;

public abstract class GetCountObserver extends ServiceObserver {
    public GetCountObserver(BasePresenter presenter, String verb) {
        super(presenter, verb);
    }

    public abstract void setCount(int count);
}
