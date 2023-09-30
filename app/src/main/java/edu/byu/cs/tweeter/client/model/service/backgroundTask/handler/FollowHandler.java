package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ToggleFollowObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowHandler extends ToggleFollowHandler {
    public FollowHandler(ToggleFollowObserver observer, User u) {
        super(observer, u);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ToggleFollowObserver obs = (ToggleFollowObserver) observer;
        obs.success(false, getSelectedUser());
        obs.setFollowButton(true);
    }
}
