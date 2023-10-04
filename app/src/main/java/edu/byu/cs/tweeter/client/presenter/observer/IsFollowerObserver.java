package edu.byu.cs.tweeter.client.presenter.observer;

import edu.byu.cs.tweeter.client.presenter.BasePresenter;

public abstract class IsFollowerObserver extends ServiceObserver {
    public IsFollowerObserver(BasePresenter presenter, String verb) {
        super(presenter, verb);
    }
    public abstract void updateFollowButton(boolean value);
}
