package edu.byu.cs.tweeter.client.model.service.observer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.AuthenticationPresenter;
import edu.byu.cs.tweeter.client.presenter.viewInterface.BaseView;
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
