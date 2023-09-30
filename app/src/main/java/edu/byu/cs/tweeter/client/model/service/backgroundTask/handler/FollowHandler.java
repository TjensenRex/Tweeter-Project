package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.observer.AddFollowObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ToggleFollowObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowHandler extends ToggleFollowHandler {
    public FollowHandler(ToggleFollowObserver observer, User u) {
        super(observer, u);
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ToggleFollowObserver a = (ToggleFollowObserver) observer;
        a.success(false, getSelectedUser());
        a.setFollowButton(true);
    }
}
