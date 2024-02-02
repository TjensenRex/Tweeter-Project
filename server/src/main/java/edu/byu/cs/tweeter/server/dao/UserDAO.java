package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.util.Pair;

import java.util.List;

public interface UserDAO {
    Pair<User, AuthToken> login(LoginRequest request);

    Pair<User, AuthToken> register(RegisterRequest request);

    Pair<Boolean, String> logout(LogoutRequest request);

    Pair<Boolean, User> getUser(GetUserRequest request) throws IllegalAccessException;

    void addUserBatch(List<User> users);
}
