package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

public interface StoryObserver extends ServiceObserver {
    void success(boolean hasMorePages, List<Status> statuses);
}
