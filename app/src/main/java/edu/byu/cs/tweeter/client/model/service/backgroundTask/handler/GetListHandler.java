package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class GetListHandler<T> extends BackgroundTaskHandler {
    public GetListHandler(ListObserver<T> observer) {
        super(observer);
    }
    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ListObserver<T> obs = (ListObserver<T>) observer;
        List<T> followers = (List<T>) data.getSerializable(GetFollowersTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFollowersTask.MORE_PAGES_KEY);
        obs.addItems(hasMorePages, followers);
    }
}
