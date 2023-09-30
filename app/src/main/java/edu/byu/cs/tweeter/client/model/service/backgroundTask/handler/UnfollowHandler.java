package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ToggleFollowObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UnfollowObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowHandler extends ToggleFollowHandler {
    public UnfollowHandler(ToggleFollowObserver observer, User selectedUser) {
        super(observer, selectedUser);
    }
    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ToggleFollowObserver obs = (ToggleFollowObserver) observer;
        obs.success(true, getSelectedUser());
        obs.setFollowButton(true);
    }
}
