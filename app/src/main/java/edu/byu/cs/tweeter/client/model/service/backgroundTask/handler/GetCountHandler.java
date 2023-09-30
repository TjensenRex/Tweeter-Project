package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.GetCountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public class GetCountHandler extends BackgroundTaskHandler {
    public GetCountHandler(GetCountObserver observer) {
        super(observer);
    }
    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        GetCountObserver obs = (GetCountObserver) observer;
        int count = data.getInt(GetCountTask.COUNT_KEY);
        obs.setCount(count);
    }
}
