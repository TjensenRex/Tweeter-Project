package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DynFollowDAO extends DynBaseDAO implements FollowDAO {
    private DynamoDbTable<UserBean> userTable;
    private DynamoDbTable<FollowRelationBean> followsTable;
    private DynamoDbTable<UserBean> getUserTable() {
     if (userTable == null) {
         userTable = getClient().table("users", TableSchema.fromBean(UserBean.class));
     }
     return userTable;
    }
    private DynamoDbTable<FollowRelationBean> getFollowsTable() {
        if (followsTable == null) {
            followsTable = getClient().table("follows", TableSchema.fromBean(FollowRelationBean.class));
        }
        return followsTable;
    }

    @Override
    public Integer getFolloweeCount(User follower) {
        UserBean user = getUser(follower.getAlias());
        return user.getFollowing();
    }

    @Override
    public Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias) {
        DynamoDbTable<FollowRelationBean> table = getFollowsTable();
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if(isNonEmptyString(lastFolloweeAlias)) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("follower_handle", AttributeValue.builder().s(followerAlias).build());
            startKey.put("followee_handle", AttributeValue.builder().s(lastFolloweeAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();


        List<User> followees = new LinkedList<>();
        AtomicBoolean hasMorePages = new AtomicBoolean(false);

        PageIterable<FollowRelationBean> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowRelationBean> page) -> {
                    hasMorePages.set(page.lastEvaluatedKey() != null);
                    page.items().forEach(followee -> followees.add(extractFollowee(followee)));
                });
        Pair<List<User>, Boolean> result = new Pair<>(followees, hasMorePages.get());
        return result;
    }
    @Override
    public int getUsersStartingIndex(String lastUserAlias, List<User> allUsers) {
        return 0;
    }
    @Override
    public Pair<List<User>, Boolean> getFollowers(String followeeAlias, int limit, String lastFollowerAlias) {
        DynamoDbIndex<FollowRelationBean> index = getFollowsTable().index("follows-index");
        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if(isNonEmptyString(lastFollowerAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("followee_handle", AttributeValue.builder().s(followeeAlias).build());
            startKey.put("follower_handle", AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<User> followers = new ArrayList<>(limit);
        AtomicBoolean hasMorePages = new AtomicBoolean(false);

        SdkIterable<Page<FollowRelationBean>> sdkIterable = index.query(request);
        PageIterable<FollowRelationBean> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowRelationBean> page) -> {
                    hasMorePages.set(page.lastEvaluatedKey() != null);
                    page.items().forEach(follower -> followers.add(extractFollower(follower)));
                });
        Pair<List<User>, Boolean> result = new Pair<>(followers, hasMorePages.get());
        return result;
    }
    @Override
    public int getFollowerCount(User followee) {
        UserBean user = getUser(followee.getAlias());
        return user.getFollowers();
    }
    @Override
    public boolean setFollow(FollowRequest request) {
        System.out.println(request.getAuthToken().getToken() + "with" + request.getAuthToken().getTimestamp());
        checkAuthToken(request.getAuthToken());
        System.out.println("entered function: " + request.getIsFollowing());
        if (request.getIsFollowing()) {
            Key key = Key.builder()
                    .partitionValue(request.getFollower().getAlias())
                    .build();
            UserBean user = getUserTable().getItem(key);
            user.setFollowing(user.getFollowing()+1);
            System.out.println("got user1");

            Key key2 = Key.builder()
                    .partitionValue(request.getFollowee().getAlias())
                    .build();
            UserBean user2 = getUserTable().getItem(key2);
            user2.setFollowers(user2.getFollowers()+1);
            System.out.println("got user2");

            getFollowsTable().putItem(new FollowRelationBean(request.getFollower().getAlias(), user.getUser(),
                    request.getFollowee().getAlias(), user2.getUser()));
            System.out.println("uploaded relation");

            getUserTable().updateItem(user);
            getUserTable().updateItem(user2);
            System.out.println("updated users");
            return true;
        }
        else {
            Key key = Key.builder()
                    .partitionValue(request.getFollower().getAlias())
                    .sortValue(request.getFollowee().getAlias())
                    .build();
            getFollowsTable().deleteItem(key);
            System.out.println("Deleted relation");

            key = Key.builder()
                    .partitionValue(request.getFollower().getAlias())
                    .build();
            UserBean user = getUserTable().getItem(key);
            user.setFollowing(user.getFollowing()-1);

            Key key2 = Key.builder()
                    .partitionValue(request.getFollowee().getAlias())
                    .build();
            UserBean user2 = getUserTable().getItem(key2);
            user2.setFollowers(user2.getFollowers()-1);

            getUserTable().updateItem(user);
            System.out.println("updated follower");
            getUserTable().updateItem(user2);
            System.out.println("updated followee");

            return true;
        }
    }

    @Override
    public boolean isFollower(String followerAlias, String followeeAlias) {
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .sortValue(followeeAlias)
                .build();
        System.out.println(followerAlias + " following " + followeeAlias);
        FollowRelationBean frBean = getFollowsTable().getItem(key);
        if (frBean != null) {
            System.out.println("true");
            return true;
        }
        else {
            System.out.println("false");
            return false;
        }
    }
    private UserBean getUser(String alias) {
        DynamoDbTable<UserBean> table = getUserTable();
        Key key = Key.builder()
                .partitionValue(alias)
                .build();

        // load it if it exists
        UserBean user = table.getItem(key);
        return user;
    }
    //DynamoDB complains about the User Type not being a type it supports if I include a get() and set() for getting
    // User objects in FollowRelationBean, so I had to put these here
    public User extractFollower(FollowRelationBean followRel) {
        return new User(followRel.getFollower_firstName(), followRel.getFollower_lastName(),
                followRel.getFollower_handle(), followRel.getFollower_image());
    }
    public User extractFollowee(FollowRelationBean followRel) {
        return new User(followRel.getFollowee_firstName(), followRel.getFollowee_lastName(),
                followRel.getFollowee_handle(), followRel.getFollowee_image());
    }

    @Override
    public void addFollowersBatch(List<String> followers, String followTarget) {
        List<FollowRelationBean> batchToWrite = new ArrayList<>();
        for (String u : followers) {
            FollowRelationBean dto = new FollowRelationBean(u, followTarget);
            batchToWrite.add(dto);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfUserDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfUserDTOs(batchToWrite);
        }
    }
    private void writeChunkOfUserDTOs(List<FollowRelationBean> userDTOs) {
        if(userDTOs.size() > 25)
            throw new RuntimeException("Too many users to write");

        //DynamoDbTable<UserDTO> able = enhancedClient.table(TableName, TableSchema.fromBean(UserDTO.class));
        WriteBatch.Builder<FollowRelationBean> writeBuilder = WriteBatch.builder(FollowRelationBean.class).mappedTableResource(getFollowsTable());
        for (FollowRelationBean item : userDTOs) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(getFollowsTable()).size() > 0) {
                writeChunkOfUserDTOs(result.unprocessedPutItemsForTable(getFollowsTable()));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }
}
