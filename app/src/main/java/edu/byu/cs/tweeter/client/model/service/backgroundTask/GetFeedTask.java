package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import android.util.Log;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {
    private static final String LOG_TAG = "GetFeedTask";
    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> queryItems() {
        try {
            String userAlias = getTargetUser().getAlias();
            Status lastStatus = getLastItem();

            StatusesRequest request = new StatusesRequest(userAlias, getAuthToken(), lastStatus, getLimit());
            StatusesResponse response = getServerFacade().getStatuses(request, StatusService.GETFEED_URL_PATH);

            if (response.isSuccess()) {
                setItems(response.getStory());
                setHasMorePages(response.getHasMorePages());

                return new Pair<>(getItems(), getHasMorePages());
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get feed", ex);
            sendExceptionMessage(ex);
        }
        return new Pair<>(null, false);
    }
}
