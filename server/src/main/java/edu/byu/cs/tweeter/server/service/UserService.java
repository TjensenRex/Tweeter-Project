package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class UserService {
    private final UserDAO userDAO;
    UserDAO getUserDAO() {
        return userDAO;
    }
    public UserService(UserDAO userdao) {
        userDAO = userdao;
    }
    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        Pair<User, AuthToken> result = getUserDAO().login(request);

        return new LoginResponse(result.getFirst(), result.getSecond());
    }

    public RegisterResponse register(RegisterRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if(request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Missing an image");
        }

        Pair<User, AuthToken> result = getUserDAO().register(request);
        return new RegisterResponse(result.getFirst(), result.getSecond());
    }
    public LogoutResponse logout(LogoutRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] alias cannot be null");
        }
        else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] authToken missing");
        }
        Pair<Boolean, String> result = getUserDAO().logout(request);

        return new LogoutResponse(result.getFirst(), result.getSecond());
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (request.getAlias() == null || request.getAlias().isEmpty()) {
            throw new RuntimeException("[Bad Request] alias cannot be empty or null");
        }
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] authToken missing");
        }
        try {
            Pair<Boolean, User> result = getUserDAO().getUser(request);
            return new GetUserResponse(result.getFirst(), result.getSecond());
        }
        catch(Exception e) {
            return new GetUserResponse(false, e.getMessage());
        }
    }
}
