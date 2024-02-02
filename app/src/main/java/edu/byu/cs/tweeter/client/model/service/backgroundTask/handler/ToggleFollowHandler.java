package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.presenter.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.observer.ToggleFollowObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class ToggleFollowHandler extends BackgroundTaskHandler {
    private final User selectedUser;
    public User getSelectedUser() {
        return selectedUser;
    }
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

    }
    public ToggleFollowHandler(ServiceObserver observer, User u) {
        super(observer);
        selectedUser = u;
    }

    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ToggleFollowObserver obs = (ToggleFollowObserver) observer;
        observerSuccess(obs);
        obs.setFollowButton(true);
    }
    abstract void observerSuccess(ToggleFollowObserver obs);
}
