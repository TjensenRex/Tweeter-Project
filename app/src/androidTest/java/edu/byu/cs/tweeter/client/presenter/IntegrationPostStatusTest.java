package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.viewInterface.AuthenticatingView;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CountDownLatch;

public class IntegrationPostStatusTest { //for milestone 4C
    private CountDownLatch countDownLatch;
    @Test
    public void test() {
        // I HATE, HATE, HATE JUNIT. IT WON'T RUN THIS F***ING TEST NO MATTER WHAT I DO
        AuthenticatingView mockView = Mockito.mock(AuthenticatingView.class);
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                countDownLatch.countDown();
                return null;
            }
        };
        System.out.println("TEST");
        Mockito.doAnswer(answer).when(mockView).displayMessage(Mockito.anyString());
        LoginPresenter loginPresenter = new LoginPresenter(mockView);
        countDownLatch = new CountDownLatch(1);
        loginPresenter.login("@jdoe", "abc123");
        System.out.println("done" + Cache.getInstance().getCurrUserAuthToken().getToken());
    }

}
