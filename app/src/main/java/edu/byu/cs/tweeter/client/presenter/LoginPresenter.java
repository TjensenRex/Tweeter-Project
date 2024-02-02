package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;
import android.widget.EditText;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.observer.AuthObserver;
import edu.byu.cs.tweeter.client.presenter.viewInterface.AuthenticatingView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The presenter for the login functionality of the application.
 */
public class LoginPresenter extends AuthenticationPresenter implements UserService.LoginObserver {

    private static final String LOG_TAG = "LoginPresenter";

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        void loginSuccessful(User user, AuthToken authToken);
        void loginUnsuccessful(String message);
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public LoginPresenter(AuthenticatingView view) {
        super(view);
        // An assertion would be better, but Android doesn't support Java assertions
        if(view == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Initiates the login process.
     *
     * @param username the user's username.
     * @param password the user's password.
     */
    public void login(String username, String password) {
        UserService userService = new UserService();
        userService.login(username, password, new AuthObserver(this, "login"));
    }

    /**
     * Invoked when the login request completes if the login was successful. Notifies the view of
     * the successful login.
     *
     * @param user the logged-in user.
     * @param authToken the session auth token.
     */
    @Override
    public void handleSuccess(User user, AuthToken authToken) {
        // Cache user session information
        Cache.getInstance().setCurrUser(user);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        //view.loginSuccessful(user, authToken);
    }

    /**
     * Invoked when the login request completes if the login request was unsuccessful. Notifies the
     * view of the unsuccessful login.
     *
     * @param message error message.
     */
    @Override
    public void handleFailure(String message) {
        String errorMessage = "Failed to login: " + message;
        Log.e(LOG_TAG, errorMessage);
        //view.loginUnsuccessful(errorMessage);
    }

    /**
     * A callback indicating that an exception occurred in an asynchronous method this class is
     * observing.
     *
     * @param exception the exception.
     */
    @Override
    public void handleException(Exception exception) {
        String errorMessage = "Failed to login because of exception: " + exception.getMessage();
        Log.e(LOG_TAG, errorMessage, exception);
        //view.loginUnsuccessful(errorMessage);
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
