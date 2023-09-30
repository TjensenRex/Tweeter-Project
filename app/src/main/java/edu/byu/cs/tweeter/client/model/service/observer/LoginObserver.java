package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface LoginObserver extends ServiceObserver {
    void loginIntent(User loggedInUser, AuthToken authToken);
}
