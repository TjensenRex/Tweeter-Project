package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface RegisterObserver extends ServiceObserver {
    void startActivity(User registeredUser, AuthToken authToken);
}
