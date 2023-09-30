package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.User;

public interface ToggleFollowObserver extends ServiceObserver {
    public void success(boolean value, User selectedUser);
    public void setFollowButton(boolean value);

}
