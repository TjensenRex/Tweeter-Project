package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.StatusBean;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class FakeStatusDAO implements StatusDAO {
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
    @Override
    public Pair<List<Status>, Boolean> getStory(StatusesRequest request) {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Status> allStatuses = getDummyStatuses();
        List<Status> responseStory = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusIndex = getStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseStory.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }

        return new Pair<>(responseStory, hasMorePages);
    }
    public int getStartingIndex(Status lastStatus, List<Status> allStatuses) {
        int statusIndex = 0;

        if(lastStatus != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastStatus.equals(allStatuses.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                    break;
                }
            }
        }

        return statusIndex;
    }

    private List<Status> getDummyStatuses() {
        return getFakeData().getFakeStatuses();
    }
    @Override
    public Pair<List<Status>, Boolean> getFeed(StatusesRequest request) {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Status> allStatuses = getDummyStatuses();
        List<Status> responseFeed = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allStatuses != null) {
                int statusIndex = getStartingIndex(request.getLastStatus(), allStatuses);

                for(int limitCounter = 0; statusIndex < allStatuses.size() && limitCounter < request.getLimit(); statusIndex++, limitCounter++) {
                    responseFeed.add(allStatuses.get(statusIndex));
                }

                hasMorePages = statusIndex < allStatuses.size();
            }
        }

        return new Pair<>(responseFeed, hasMorePages);
    }

    @Override
    public Pair<Boolean, String> postStatus(PostStatusRequest request) {
        try {
            return new Pair<>(true, "Successfully Posted!");
        }
        catch (Exception e) {
            return new Pair<>(false, "failed to post the status.");
        }
    }

    @Override
    public void updateFeeds(String alias, StatusBean statusBean) {

    }
}
