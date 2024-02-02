package edu.byu.cs.tweeter.client.model.net;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ServerFacadeIntegrationTest {
    private ServerFacade serverFacade;
    @BeforeEach
    public void setup() {
        serverFacade = new ServerFacade();
    }
    @Test
    public void TestRegister() {
        RegisterRequest request = new RegisterRequest("@jdoe", "abc123", "John",
                "Doe", "I'm an image! ;)");
        try {
            RegisterResponse response = serverFacade.register(request, "/register");
            assert(response.isSuccess());
            assert(response.getAuthToken() != null);
            assert(response.getUser() != null);
        } catch (Exception e) {
            fail();
        }
    }
    @Test
    public void TestGetFollowers() {
        AuthToken authToken = new AuthToken();
        authToken.setToken("abc");
        FollowersRequest request = new FollowersRequest(authToken, "@jdoe", 10, null);
        try {
            FollowersResponse response = serverFacade.getFollowers(request, "/getfollowers");
            assert(response.isSuccess());
            assert(!response.getFollowers().isEmpty());
        } catch (Exception e) {
            fail();
        }
    }
    @Test
    public void TestGetFollowersCount() {
        User user = new User("John", "Doe", "@jdoe", "I'm an image! ;)");
        AuthToken authToken = new AuthToken();
        authToken.setToken("abc");
        CountRequest request = new CountRequest(user, authToken);
        try {
            CountResponse response = serverFacade.getCount(request, "/getfollowercount");
            assert(response.isSuccess());
            assert(response.getCount() > 0);
        } catch (Exception e) {
            fail();
        }
    }
}
