package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.http.HttpResponse;

public abstract class DynBaseDAO {
    protected static DynamoDbEnhancedClient enhancedClient;
    protected DynamoDbEnhancedClient getClient() {
        if (enhancedClient == null)
        {
            enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(DynamoDbClient.builder()
                            .region(Region.US_WEST_1)
                            .build())
                    .build();
        }
        return enhancedClient;
    }
    protected boolean checkAuthToken(AuthToken authToken) {
        long curr_time = System.currentTimeMillis();
        DynamoDbTable<AuthTokenBean> table = getClient().table("authToken", TableSchema.fromBean(AuthTokenBean.class));
        Key key = Key.builder()
                .partitionValue(authToken.getToken())
                .build();
        AuthTokenBean bean = table.getItem(key);
        if (bean == null) {
            throw new RuntimeException("AuthToken is invalid.");
        }
        if (curr_time - bean.getLastUsedTimestamp() > 300000) {
            throw new RuntimeException("AuthToken is invalid.");
        }
        table.updateItem(new AuthTokenBean(authToken.getToken(), curr_time));
        return true;
    }
    protected boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }
}
