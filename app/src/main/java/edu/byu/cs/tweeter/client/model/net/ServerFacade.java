package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.*;
import edu.byu.cs.tweeter.model.net.response.*;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {
    private static final String SERVER_URL = "URL_HERE";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);
    }

    public FollowersResponse getFollowers(FollowersRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, FollowersResponse.class);
    }

    public RegisterResponse register(RegisterRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, RegisterResponse.class);
    }

    public CountResponse getCount(CountRequest request, String followingCountUrl) throws IOException, TweeterRemoteException {
        //Map<String, String> headers = new TreeMap<>();
        //headers.put("user", request.getUser().getAlias());
        return clientCommunicator.doPost(followingCountUrl, request,null, CountResponse.class);
    }

    public StatusesResponse getStatuses(StatusesRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, StatusesResponse.class);
    }

    public FollowResponse follow(FollowRequest request, String url) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(url, request, null, FollowResponse.class);
    }

    public LogoutResponse logout(LogoutRequest request, String logoutUrlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(logoutUrlPath, request, null, LogoutResponse.class);
    }

    public GetUserResponse getUser(GetUserRequest request, String urlPath) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(urlPath, request, null, GetUserResponse.class);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request, String isFollowerUrl) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(isFollowerUrl, request, null, IsFollowerResponse.class);
    }

    public PostStatusResponse postStatus(PostStatusRequest request, String postStatusUrl) throws IOException, TweeterRemoteException {
        return clientCommunicator.doPost(postStatusUrl, request, null, PostStatusResponse.class);
    }
}
