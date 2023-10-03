package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.AuthObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.AuthenticatingView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthenticationPresenter {
    public LoginPresenter(AuthenticatingView view) {
        super(view);
    }
    public void login(String alias, String password) {
        getUserService().login(alias, password, new AuthObserver(this, "login"));
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
}
