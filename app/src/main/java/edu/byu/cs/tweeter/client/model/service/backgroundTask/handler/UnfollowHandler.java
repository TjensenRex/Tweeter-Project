package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.presenter.observer.ToggleFollowObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowHandler extends ToggleFollowHandler {
    public UnfollowHandler(ToggleFollowObserver observer, User selectedUser) {
        super(observer, selectedUser);
    }
    @Override
    void observerSuccess(ToggleFollowObserver obs) {
        obs.success(true, getSelectedUser());
    }
}
