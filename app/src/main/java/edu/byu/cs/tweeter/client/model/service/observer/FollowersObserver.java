package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public interface FollowersObserver extends ServiceObserver {
    void addFollowers(boolean hasMorePages, List<User> followers);
}
