package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.*;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticatedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticatingHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LoginTaskHandler;
import edu.byu.cs.tweeter.client.presenter.observer.AuthObserver;
import edu.byu.cs.tweeter.client.presenter.observer.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.presenter.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Contains the business logic to support the login operation.
 */
public class UserService extends BaseService {

    public static final String URL_PATH = "/login";
    public static final String REGISTER_URL = "/register";
    public static final String LOGOUT_URL_PATH = "/logout";
    public static final String GETUSER_URL_PATH = "/getuser";

    /**
     * An observer interface to be implemented by observers who want to be notified when
     * asynchronous operations complete.
     */
    public interface LoginObserver {
        void handleSuccess(User user, AuthToken authToken);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    /**
     * Makes an asynchronous login request.
     *
     * @param username the user's name.
     * @param password the user's password.
     */
    public void login(String username, String password, AuthObserver observer) {
        LoginTask loginTask = getLoginTask(username, password, observer);
        BackgroundTaskUtils.runTask(loginTask);
    }

    /**
     * Returns an instance of {@link LoginTask}. Allows mocking of the LoginTask class for
     * testing purposes. All usages of LoginTask should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    LoginTask getLoginTask(String username, String password, AuthObserver observer) {
        return new LoginTask(this, username, password, new LoginTaskHandler(observer));
    }
    public void register(String firstName, String lastName, String alias, String password,
                         String imageBytesBase64, AuthObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new AuthenticatingHandler(observer));
        executeTask(registerTask);
    }
    public void logout(AuthToken currUserAuthToken, AuthenticatedObserver observer) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new AuthenticatedHandler(observer));
        executeTask(logoutTask);
    }
    public void getUser(AuthToken currUserAuthToken, String userAlias, UserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken, userAlias, new GetUserHandler(observer));
        executeTask(getUserTask);
    }
}
