package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public class IsFollowerHandler extends BackgroundTaskHandler {
    public IsFollowerHandler(IsFollowerObserver observer) {
        super(observer);
    }
    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        IsFollowerObserver obs = (IsFollowerObserver) observer;
        // If logged in user if a follower of the selected user, display the follow button as "following"
        if (isFollower) {
            obs.updateFollowButton(false);
        } else {
            obs.updateFollowButton(true);
        }
    }
}
