package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.viewInterface.AuthenticatingView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticationPresenter extends BasePresenter {
    private final UserService userService;
    public UserService getUserService() {
        return userService;
    }
    public AuthenticationPresenter(AuthenticatingView view) {
        super(view);
        userService = new UserService();
    }
    public AuthenticatingView getView() {
        return (AuthenticatingView) super.getView();
    }
    public void startViewActivity(User user, AuthToken authToken) {
        Cache.getInstance().setCurrUser(user);
        Cache.getInstance().setCurrUserAuthToken(authToken);
        getView().startViewActivity(user, Cache.getInstance().getCurrUser().getName());
    }
}
