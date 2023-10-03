package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.viewInterface.AuthenticatingView;

public class AuthenticationPresenter extends BasePresenter {
    private UserService userService;
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
}
