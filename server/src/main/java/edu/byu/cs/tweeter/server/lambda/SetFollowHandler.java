package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDBModule;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.service.FollowService;

public class SetFollowHandler implements RequestHandler<FollowRequest, FollowResponse> {
    @Override
    public FollowResponse handleRequest(FollowRequest request, Context context) {
        Injector injector = Guice.createInjector(new DynamoDBModule());
        FollowService service = new FollowService(injector.getInstance(FollowDAO.class));
        return service.setFollow(request);
    }
}
