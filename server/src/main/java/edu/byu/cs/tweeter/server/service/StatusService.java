package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.FakeStatusDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.util.Pair;

import java.util.List;

public class StatusService {
    private final StatusDAO statusDAO;
    StatusDAO getStatusDAO() {
        return statusDAO;
    }
    public StatusService(StatusDAO statusDAO) {
        this.statusDAO = statusDAO;
    }
    public StatusesResponse getStory(StatusesRequest request) {
        Pair<List<Status>, Boolean> result = getStatusDAO().getStory(request);
        return new StatusesResponse(result.getFirst(), result.getSecond());
    }

    public StatusesResponse getFeed(StatusesRequest request) {
        Pair<List<Status>, Boolean> result = getStatusDAO().getFeed(request);
        return new StatusesResponse(result.getFirst(), result.getSecond());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        try {
            Pair<Boolean, String> result = getStatusDAO().postStatus(request);
            return new PostStatusResponse(result.getFirst(), result.getSecond());
        }
        catch (Exception e) {
            return new PostStatusResponse(false, "failed to post the status.");
        }
    }
}
