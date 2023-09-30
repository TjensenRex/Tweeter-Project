package edu.byu.cs.tweeter.client.model.service.observer;

public interface GetFollowingCountObserver extends ServiceObserver {
    void setCount(int count);
}
