package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticatedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetListHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ListObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatusService {
    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus,
                        ListObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken, user, pageSize, lastStatus,
                new GetListHandler<Status>(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }
    public void post(AuthToken currUserAuthToken, Status newStatus, AuthenticatedObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken, newStatus, new AuthenticatedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }
    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus,
                         ListObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken, user, pageSize, lastStatus,
                new GetListHandler<Status>(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }
}
