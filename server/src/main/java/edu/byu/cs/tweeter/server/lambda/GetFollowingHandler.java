package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDBModule;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowingHandler implements RequestHandler<FollowingRequest, FollowingResponse> {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followees.
     */
    @Override
    public FollowingResponse handleRequest(FollowingRequest request, Context context) {
        Injector injector = Guice.createInjector(new DynamoDBModule());
        FollowService service = new FollowService(injector.getInstance(FollowDAO.class));
        System.out.println(request.getAuthToken() + " " + request.getFollowerAlias() + " " +
                request.getLastFolloweeAlias() + " " + request.getLimit());
        return service.getFollowees(request);
    }
}
