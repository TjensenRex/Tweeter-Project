package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDBModule;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<FollowersRequest, FollowersResponse> {
    @Override
    public FollowersResponse handleRequest(FollowersRequest request, Context context) {
        Injector injector = Guice.createInjector(new DynamoDBModule());
        FollowService service = new FollowService(injector.getInstance(FollowDAO.class));
        System.out.println(request.getAuthToken() + " " + request.getFolloweeAlias() + " " +
                request.getLastFollowerAlias() + " " + request.getLimit());
        return service.getFollowers(request);
    }
}
