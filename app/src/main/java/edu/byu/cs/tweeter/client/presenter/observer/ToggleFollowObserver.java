package edu.byu.cs.tweeter.client.presenter.observer;

import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class ToggleFollowObserver extends ServiceObserver {
    public ToggleFollowObserver(BasePresenter presenter, String verb) {
        super(presenter, verb);
    }
    public abstract void success(boolean value, User selectedUser);
    public abstract void setFollowButton(boolean value);

}
