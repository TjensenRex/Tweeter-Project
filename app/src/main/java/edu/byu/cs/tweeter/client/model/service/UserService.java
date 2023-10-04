package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticatedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticatingHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.presenter.observer.AuthObserver;
import edu.byu.cs.tweeter.client.presenter.observer.AuthenticatedObserver;
import edu.byu.cs.tweeter.client.presenter.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserService extends BaseService {
    public void login(String alias, String password, AuthObserver observer) {
        LoginTask loginTask = new LoginTask(alias, password, new AuthenticatingHandler(observer));
        executeTask(loginTask);
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
