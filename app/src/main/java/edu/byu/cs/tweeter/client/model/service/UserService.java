package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.*;
import edu.byu.cs.tweeter.client.model.service.observer.*;
import edu.byu.cs.tweeter.model.domain.AuthToken;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserService {
    public void login(String alias, String password, AuthObserver observer) {
        LoginTask loginTask = new LoginTask(alias, password, new AuthenticatingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }
    public void register(String firstName, String lastName, String alias, String password,
                         String imageBytesBase64, AuthObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new AuthenticatingHandler(observer));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }
    public void logout(AuthToken currUserAuthToken, AuthenticatedObserver observer) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new AuthenticatedHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }
    public void getUser(AuthToken currUserAuthToken, String userAlias, UserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAlias, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }
}
