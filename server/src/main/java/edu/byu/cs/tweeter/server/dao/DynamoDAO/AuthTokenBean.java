package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class AuthTokenBean {
    /**
     * Value of the auth token.
     */
    private String token;
    private long lastUsedTimestamp;

    public AuthTokenBean() {
    }

    public AuthTokenBean(String token, long lastUsedTimestamp) {
        this.token = token;
        this.lastUsedTimestamp = lastUsedTimestamp;
    }
    @DynamoDbPartitionKey
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public long getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }
    public void setLastUsedTimestamp(long lastUsedTimestamp) {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }
}
