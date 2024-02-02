package edu.byu.cs.tweeter.server.dao;

import com.google.inject.AbstractModule;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DynFollowDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DynStatusDAO;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.DynUserDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

public class DynamoDBModule extends AbstractModule {
    public DynamoDBModule() {}
    @Override
    public void configure() {
        bind(UserDAO.class).to(DynUserDAO.class);
        bind(StatusDAO.class).to(DynStatusDAO.class);
        bind(FollowDAO.class).to(DynFollowDAO.class);
    }
}
