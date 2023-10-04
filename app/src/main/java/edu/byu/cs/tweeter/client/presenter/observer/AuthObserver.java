package edu.byu.cs.tweeter.client.presenter.observer;

import edu.byu.cs.tweeter.client.presenter.AuthenticationPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthObserver extends ServiceObserver {
    public AuthObserver(AuthenticationPresenter p, String verb) {
        super(p, verb);
    }
    public void startIntent(User registeredUser, AuthToken authToken) {
        getPresenter().startViewActivity(registeredUser, authToken);
    }
    @Override
    public AuthenticationPresenter getPresenter() {
        return (AuthenticationPresenter) super.getPresenter();
    }
}
