package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

/**
 * Message handler (i.e., observer) for GetFeedTask.
 */
public class GetFeedHandler extends BackgroundTaskHandler {
    public GetFeedHandler(ListObserver<Status> observer) {
        super(observer);
    }
    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        ListObserver<Status> obs = (ListObserver<Status>) observer;
        List<Status> statuses = (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
        obs.addItems(hasMorePages, statuses);
    }
}
