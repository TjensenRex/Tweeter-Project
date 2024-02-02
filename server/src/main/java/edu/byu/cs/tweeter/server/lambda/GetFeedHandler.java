package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.model.net.response.StatusesResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDBModule;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetFeedHandler implements RequestHandler<StatusesRequest, StatusesResponse> {

    @Override
    public StatusesResponse handleRequest(StatusesRequest request, Context context) {
        Injector injector = Guice.createInjector(new DynamoDBModule());
        StatusService service = new StatusService(injector.getInstance(StatusDAO.class));
        return service.getFeed(request);
    }
}
