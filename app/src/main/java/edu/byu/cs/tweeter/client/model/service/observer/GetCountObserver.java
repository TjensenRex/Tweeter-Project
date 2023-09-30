package edu.byu.cs.tweeter.client.model.service.observer;

public interface GetCountObserver extends ServiceObserver {
    public void setCount(int count);
}
