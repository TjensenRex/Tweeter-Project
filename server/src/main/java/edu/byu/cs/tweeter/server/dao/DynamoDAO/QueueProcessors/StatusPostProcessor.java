package edu.byu.cs.tweeter.server.dao.DynamoDAO.QueueProcessors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DynFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.JsonSerializer;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.StatusBean;
import edu.byu.cs.tweeter.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class StatusPostProcessor implements RequestHandler<SQSEvent, Void> {
    private final String updateFeedQueueUrl = "URL_HERE";
    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println(msg.getBody());
            StatusBean statusBean = JsonSerializer.deserialize(msg.getBody(), StatusBean.class);
            DynFollowDAO followDAO = new DynFollowDAO();
            Pair<List<User>, Boolean> result = followDAO.getFollowers(statusBean.getPosterAlias(), 25,
                    null);
            List<String> followers = result.getFirst().stream().map((user) -> {return user.getAlias();})
                    .collect(Collectors.toUnmodifiableList());
            System.out.println("list converted");

            FeedMessage message = new FeedMessage(statusBean, followers);

            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(updateFeedQueueUrl)
                    .withMessageBody(JsonSerializer.serialize(message));
            AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
            sqs.sendMessage(send_msg_request);

            while (result.getSecond()) {
                result = followDAO.getFollowers(statusBean.getPosterAlias(), 25,
                        result.getFirst().get(result.getFirst().size()-1).getAlias());
                followers = result.getFirst().stream().map((user) -> {return user.getAlias();})
                        .collect(Collectors.toUnmodifiableList());

                message = new FeedMessage(statusBean, followers);

                send_msg_request = new SendMessageRequest()
                        .withQueueUrl(updateFeedQueueUrl)
                        .withMessageBody(JsonSerializer.serialize(message));
                sqs.sendMessage(send_msg_request);
            }
            System.out.println("updated Feed queue");
        }
        return null;
    }
}
