package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Objects;

public class MainActivityPresenterUnitTest {
    private MainActivityPresenter.MainView mockView;
    private StatusService mockService;
    private Cache mockCache;
    private MainActivityPresenter mainAPSpy;
    @BeforeEach
    public void setup() {
        //create mocks
        mockView = Mockito.mock(MainActivityPresenter.MainView.class);
        mockService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);
        mainAPSpy = Mockito.spy(new MainActivityPresenter(mockView));
        Mockito.when(mainAPSpy.getStatusService()).thenReturn(mockService);
        Cache.setInstance(mockCache);
    }
    private void verifyErrorResult(String message) {
        Mockito.verify(mockView, Mockito.times(0)).postSuccess();
        Mockito.verify(mockView).displayMessage(message);
    }
    private void postAndVerifyStart(Answer<Void> answer, String message) {
        Mockito.doAnswer(answer).when(mockService).post(Mockito.any(), Mockito.any(), Mockito.any());
        mainAPSpy.post(message);
        Mockito.verify(mockView).showPostToast();
    }
    @Test
    public void testPost_showToast() {
    }
    private class StatusMatch implements ArgumentMatcher<Status> {
        private final String post;
        public StatusMatch(String post) {
            this.post = post;
        }
        @Override
        public boolean matches(Status argument) {
            return Objects.equals(argument.post, this.post);
        }
        public String toString() {
            return "Status with post value of: " + post;
        }
    }
    @Test
    public void testPost_ServiceParamTypes() {
        Mockito.when(mockCache.getCurrUserAuthToken()).thenReturn(new AuthToken());
        mainAPSpy.post("foo");
        // Mockito.argThat() with the StatusMatch custom inner class verifies that the passed Status has the right .post value
        Mockito.verify(mockService).post(Mockito.any(AuthToken.class), Mockito.argThat(new StatusMatch("foo")),
                Mockito.any(MainActivityPresenter.PostObserver.class));
    }
    @Test
    public void testPost_successful() {
        String message = "foo";
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainActivityPresenter.PostObserver observer = invocation.getArgument(2, MainActivityPresenter.PostObserver.class);
                observer.success();
                return null;
            }
        };
        postAndVerifyStart(answer, message);
        Mockito.verify(mockView).postSuccess();
    }
    @Test
    public void testPost_failure() {
        String message = "foo";
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainActivityPresenter.PostObserver observer = invocation.getArgument(2, MainActivityPresenter.PostObserver.class);
                observer.handleFailure(message);
                return null;
            }
        };
        postAndVerifyStart(answer, message);
        verifyErrorResult("Failed to post status: " + message);
    }
    @Test
    public void testPost_exception() {
        String message = "foo";
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainActivityPresenter.PostObserver observer = invocation.getArgument(2, MainActivityPresenter.PostObserver.class);
                observer.handleException(new Exception(message));
                return null;
            }
        };
        postAndVerifyStart(answer, message);
        verifyErrorResult("Failed to post status because of exception: " + message);
    }
}
