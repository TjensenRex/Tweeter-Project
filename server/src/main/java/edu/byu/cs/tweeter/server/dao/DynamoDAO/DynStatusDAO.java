package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StatusesRequest;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DynStatusDAO extends DynBaseDAO implements StatusDAO {
    DynamoDbTable<FeedBean> feedTable;
    DynamoDbTable<FeedBean> getFeedTable() {
        if (feedTable == null) {
            feedTable = getClient().table("feed", TableSchema.fromBean(FeedBean.class));
        }
        return feedTable;
    }
    /*I know getStory() and getFeed() share a lot of similarities, but I didn't want to deal with any potential
    * hangups with how dynamodb beans would react to inheritance.*/
    @Override
    public Pair<List<Status>, Boolean> getStory(StatusesRequest request) {
        checkAuthToken(request.getAuthToken());
        DynamoDbTable<StatusBean> table = getClient().table("story", TableSchema.fromBean(StatusBean.class));
        Key key = Key.builder()
                .partitionValue(request.getUserAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false)
                .limit(request.getLimit());

        if(request.getLastStatus() != null) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("posterAlias", AttributeValue.builder().s(request.getUserAlias()).build());
            startKey.put("timestamp", AttributeValue.builder()
                    .n(Long.toString(request.getLastStatus().getTimestamp())).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest req2 = requestBuilder.build();

        List<Status> story = new LinkedList<>();
        AtomicBoolean hasMorePages = new AtomicBoolean(false);

        PageIterable<StatusBean> pages = table.query(req2);
        pages.stream()
                .limit(1)
                .forEach((Page<StatusBean> page) -> {
                    hasMorePages.set(page.lastEvaluatedKey() != null);
                    page.items().forEach(status -> story.add(status.extractStatus()));
                });
        Pair<List<Status>, Boolean> result = new Pair<>(story, hasMorePages.get());

        return result;
    }
    @Override
    public Pair<List<Status>, Boolean> getFeed(StatusesRequest request) {
        checkAuthToken(request.getAuthToken());
        DynamoDbTable<FeedBean> table = getFeedTable();
        Key key = Key.builder()
                .partitionValue(request.getUserAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .scanIndexForward(false)
                .limit(request.getLimit());

        if(request.getLastStatus() != null) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("receiverAlias", AttributeValue.builder().s(request.getUserAlias()).build());
            startKey.put("timestamp", AttributeValue.builder()
                    .n(Long.toString(request.getLastStatus().getTimestamp())).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest req2 = requestBuilder.build();

        List<Status> story = new LinkedList<>();
        AtomicBoolean hasMorePages = new AtomicBoolean(false);

        PageIterable<FeedBean> pages = table.query(req2);
        pages.stream()
                .limit(1)
                .forEach((Page<FeedBean> page) -> {
                    hasMorePages.set(page.lastEvaluatedKey() != null);
                    page.items().forEach(status -> story.add(status.extractStatus()));
                });
        Pair<List<Status>, Boolean> result = new Pair<>(story, hasMorePages.get());

        return result;
    }
    @Override
    public Pair<Boolean, String> postStatus(PostStatusRequest request) {
        String postStatusQueueUrl = "URL_HERE";

        checkAuthToken(request.getAuthToken());
        DynamoDbTable<StatusBean> table = getClient().table("story", TableSchema.fromBean(StatusBean.class));
        StatusBean statusBean = new StatusBean(request.getStatus());
        table.putItem(statusBean);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(postStatusQueueUrl)
                .withMessageBody(JsonSerializer.serialize(statusBean));

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(send_msg_request);

        return new Pair<>(true, "Successfully Posted!");
    }
    public void updateFeeds(String alias, StatusBean statusBean) {
        getFeedTable().putItem(new FeedBean(statusBean, alias));
    }
}
