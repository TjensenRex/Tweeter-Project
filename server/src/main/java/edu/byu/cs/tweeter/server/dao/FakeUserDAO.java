package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

import java.util.List;

public class FakeUserDAO implements UserDAO {
    @Override
    public Pair<User, AuthToken> login(LoginRequest request) {
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new Pair<>(user, authToken);
    }

    @Override
    public Pair<User, AuthToken> register(RegisterRequest request) {

        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new Pair<>(user, authToken);
    }

    @Override
    public Pair<Boolean, String> logout(LogoutRequest request) {
        return new Pair<>(true, "Successfully logged out.");
    }

    @Override
    public Pair<Boolean, User> getUser(GetUserRequest request) {
        User user = getFakeData().findUserByAlias(request.getAlias());
        return new Pair<>(true, user);
    }

    @Override
    public void addUserBatch(List<User> users) {

    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
