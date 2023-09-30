package edu.byu.cs.tweeter.client.model.service.observer;

public interface GetFollowersCountObserver extends ServiceObserver {
    void setCount(int count);
}
