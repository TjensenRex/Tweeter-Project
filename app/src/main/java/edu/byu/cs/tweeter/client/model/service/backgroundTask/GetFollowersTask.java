package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;

import java.io.IOException;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {
    private static final String LOG_TAG = "GetFollowersTask";
    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }
    @Override
    protected void runTask() {
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            String lastFolloweeAlias = getLastItem() == null ? null : getLastItem().getAlias();

            FollowersRequest request = new FollowersRequest(Cache.getInstance().getCurrUserAuthToken(),
                    targetUserAlias, getLimit(), lastFolloweeAlias);
            FollowersResponse response = getServerFacade().getFollowers(request, FollowService.FOLLOWERS_URL_PATH);

            if (response.isSuccess()) {
                setItems(response.getFollowers());
                //this.followees = response.getFollowees();
                setHasMorePages(response.getHasMorePages());
                //this.hasMorePages = response.getHasMorePages();
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followers", ex);
            sendExceptionMessage(ex);
        }
    }
}
