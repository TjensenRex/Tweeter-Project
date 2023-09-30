package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public interface FollowingObserver extends ServiceObserver {
    void addMoreFollowees(List<User> followees, boolean hasMorePages);
}
