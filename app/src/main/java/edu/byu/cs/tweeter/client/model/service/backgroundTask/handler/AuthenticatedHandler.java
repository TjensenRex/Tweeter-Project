package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.presenter.observer.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.presenter.observer.ServiceObserver;

public class AuthenticatedHandler extends BackgroundTaskHandler {
    public AuthenticatedHandler(ServiceObserver observer) {
        super(observer);
    }
    @Override
    protected void handleSuccessMessage(ServiceObserver observer, Bundle data) {
        AuthenticatedObserver obs = (AuthenticatedObserver) observer;
        obs.success();
    }
}
