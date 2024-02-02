package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.CountRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;

import java.io.IOException;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {
        CountRequest request = new CountRequest(getTargetUser(), getAuthToken());
        try {
            CountResponse response = getServerFacade().getCount(request, FollowService.FOLLOWING_COUNT_URL);
            if (response.isSuccess()) {
                return response.getCount();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (Exception e) {
            sendExceptionMessage(e);
        }
        return -1;
    }
}
