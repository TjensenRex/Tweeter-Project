package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.AuthObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.AuthenticatingView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    public interface View {
    }
    private AuthenticatingView view;
    private UserService userService;
    public LoginPresenter(AuthenticatingView view) {
        this.view = view;
        userService = new UserService();
    }
    public void login(String alias, String password) {
        userService.login(alias, password, new UserServiceObserver());
    }
    public void validateLogin(EditText alias, EditText password) {
        if (alias.getText().length() > 0 && alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }
    public class UserServiceObserver implements AuthObserver {
        @Override
        public void startIntent(User loggedInUser, AuthToken authToken) {
            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.startActivity(loggedInUser, Cache.getInstance().getCurrUser().getName());
        }
        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to login: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }
}
