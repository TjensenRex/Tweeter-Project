package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface ListObserver<T> extends ServiceObserver {
    abstract void addItems(boolean hasMorePages, List<T> items);
}
