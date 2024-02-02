package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import android.util.Log;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

public abstract class PagedUserTask extends PagedTask<User> {
    protected PagedUserTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }
    @Override
    protected final List<User> getUsersForItems(List<User> items) {
        return items;
    }
}
