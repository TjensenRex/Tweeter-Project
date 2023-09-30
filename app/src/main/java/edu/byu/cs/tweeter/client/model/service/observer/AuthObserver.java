package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthObserver extends ServiceObserver {
    void startIntent(User loggedInUser, AuthToken authToken);
}
