package edu.byu.cs.tweeter.server.dao.DynamoDAO.QueueProcessors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.JsonSerializer;
import edu.byu.cs.tweeter.server.dao.DynamoDBModule;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class FeedUpdateProcessor implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        Injector injector = Guice.createInjector(new DynamoDBModule());
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            //System.out.println(msg.getBody());
            FeedMessage feedMessage = JsonSerializer.deserialize(msg.getBody(), FeedMessage.class);
            StatusDAO statusDAO = injector.getInstance(StatusDAO.class);
            for (String alias : feedMessage.getFollowers()) {
                statusDAO.updateFeeds(alias, feedMessage.getStatusBean());
            }
        }
        return null;
    }
}
