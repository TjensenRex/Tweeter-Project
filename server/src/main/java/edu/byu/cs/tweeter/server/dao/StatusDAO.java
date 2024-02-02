package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.StatusBean;
import edu.byu.cs.tweeter.util.Pair;

import java.util.List;

public interface StatusDAO {

    Pair<List<Status>, Boolean> getStory(StatusesRequest request);

    Pair<List<Status>, Boolean> getFeed(StatusesRequest request);

    Pair<Boolean, String> postStatus(PostStatusRequest request);

    void updateFeeds(String alias, StatusBean statusBean);
}
