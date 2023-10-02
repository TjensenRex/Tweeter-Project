package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.observer.ToggleFollowObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowHandler extends ToggleFollowHandler {
    public FollowHandler(ToggleFollowObserver observer, User u) {
        super(observer, u);
    }
    @Override
    void observerSuccess(ToggleFollowObserver obs) {
        obs.success(false, getSelectedUser());
    }
}
