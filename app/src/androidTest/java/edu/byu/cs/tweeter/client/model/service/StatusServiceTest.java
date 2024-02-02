package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.client.presenter.observer.ListObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class StatusServiceTest {
    private StatusService statusService;
    private CountDownLatch latch;
    private class FollowServiceObsesrver extends ListObserver<Status> {
        private boolean success;
        private String message;
        private Exception ex;
        private List<Status> story;
        private boolean hasMorePages;
        public FollowServiceObsesrver(PagedPresenter<Status> p, String t) {
            super(p, t);
        }
        @Override
        public void addItems(boolean hasMorePages, List<Status> story) {
            success = true;
            message = null;
            this.story = story;
            this.hasMorePages = hasMorePages;
            ex = null;
            latch.countDown();
        }
    }
    private void resetCountDownLatch() {
        latch = new CountDownLatch(1);
    }
    private void awaitCountDownLatch() throws InterruptedException {
        latch.await();
        resetCountDownLatch();
    }

    @BeforeEach
    public void setup() {
        statusService = new StatusService();
        resetCountDownLatch();
    }
    @Test
    public void TestGetStory() throws InterruptedException {
        User user = new User("John", "Doe", "@jdoe", "I'm an image! ;)");
        AuthToken authToken = new AuthToken();
        authToken.setToken("abc");
        //PagedPresenter<Status> mockPresenter = Mockito.mock(StoryPresenter.class);
        PagedPresenter<Status> presenter = Mockito.mock(PagedPresenter.class);
        FollowServiceObsesrver observer = new FollowServiceObsesrver(presenter, "get story");
        statusService.getStory(authToken, user, 10, null, observer);
        awaitCountDownLatch();
        Mockito.verify(observer).addItems(Mockito.anyBoolean(), Mockito.anyList());
        assert(observer.success);
    }
}
