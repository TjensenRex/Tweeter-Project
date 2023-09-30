package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.observer.FollowersObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

/**
 * Message handler (i.e., observer) for GetFollowersTask.
 */
public class GetFollowersHandler extends BackgroundTaskHandler {
    public GetFollowersHandler(ListObserver<User> observer) {
        super(observer);
    }
    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ListObserver<User> obs = (ListObserver<User>) observer;
        List<User> followers = (List<User>) data.getSerializable(GetFollowersTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFollowersTask.MORE_PAGES_KEY);
        obs.addItems(hasMorePages, followers);
    }
}
