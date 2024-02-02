package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class StatusesRequest {
    private AuthToken authToken;
    private String userAlias;
    private Status lastStatus;
    private int limit;
    public StatusesRequest() {}
    public StatusesRequest(String userAlias, AuthToken authToken, Status lastStatus, int limit) {
        this.userAlias = userAlias;
        this.authToken = authToken;
        this.lastStatus = lastStatus;
        this.limit = limit;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
    public String getUserAlias() {
        return userAlias;
    }
    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
    public Status getLastStatus() {
        return lastStatus;
    }
    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }
}
