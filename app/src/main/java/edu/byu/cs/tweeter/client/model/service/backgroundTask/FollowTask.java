package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    /**
     * The user that is being followed.
     */
    private final User followee;
    //private String followerAlias;

    public FollowTask(AuthToken authToken, User followee, /*String followeralias,*/ Handler messageHandler) {
        super(authToken, messageHandler);
        //this.followerAlias = followeralias;
        this.followee = followee;
    }

    @Override
    protected void runTask() {
        try {
            FollowRequest request = new FollowRequest(Cache.getInstance().getCurrUser(), followee,
                    getAuthToken(), true);
            FollowResponse response = getServerFacade().follow(request, FollowService.SET_FOLLOW_URL);
            if (response.isSuccess()) {
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (Exception e) {
            sendExceptionMessage(e);
        }

        // Call sendSuccessMessage if successful
        // or call sendFailedMessage if not successful
        // sendFailedMessage()
    }

}
