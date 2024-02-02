package edu.byu.cs.tweeter.server.dao.DynamoDAO.QueueProcessors;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.DynamoDAO.StatusBean;

import java.util.List;

public class FeedMessage {
    private StatusBean statusBean;
    private List<String> followers;
    public FeedMessage() {}
    public FeedMessage(StatusBean statusBean, List<String> followers) {
        this.statusBean = statusBean;
        this.followers = followers;
    }
    public StatusBean getStatusBean() {
        return statusBean;
    }
    public void setStatusBean(StatusBean statusBean) {
        this.statusBean = statusBean;
    }
    public List<String> getFollowers() {
        return followers;
    }
    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }
}
