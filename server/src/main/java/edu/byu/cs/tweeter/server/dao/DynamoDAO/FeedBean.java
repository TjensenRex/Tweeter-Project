package edu.byu.cs.tweeter.server.dao.DynamoDAO;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;
/*
* Yes, I know this bears a great resemblance to the StatusBean, but I just don't want to deal with figuring out how
* DynamoDbBeans deal with inheritance and whether child partition keys override parent partition keys, etc. */
@DynamoDbBean
public class FeedBean {
    private String post;
    private Long timestamp;
    private List<String> urls;
    private List<String> mentions;
    private String receiverAlias;
    private String posterAlias;
    private String poster_firstName;
    private String poster_lastName;
    private String poster_image;
    public FeedBean() {}
    public FeedBean(StatusBean status, String alias) {
        this.post = status.getPost();
        this.timestamp = status.getTimestamp();
        this.urls = status.getUrls();
        this.mentions = status.getMentions();

        posterAlias = status.getPosterAlias();
        poster_firstName = status.getPoster_firstName();
        poster_lastName = status.getPoster_lastName();
        poster_image = status.getPoster_image();

        receiverAlias = alias;
    }
    public String getPost() {
        return post;
    }
    public void setPost(String post) {
        this.post = post;
    }
    @DynamoDbSortKey
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public List<String> getUrls() {
        return urls;
    }
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
    public List<String> getMentions() {
        return mentions;
    }
    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
    @DynamoDbPartitionKey
    public String getReceiverAlias() {
        return receiverAlias;
    }
    public void setReceiverAlias(String receiverAlias) {
        this.receiverAlias = receiverAlias;
    }
    public String getPosterAlias() {
        return posterAlias;
    }
    public void setPosterAlias(String posterAlias) {
        this.posterAlias = posterAlias;
    }
    public String getPoster_firstName() {
        return poster_firstName;
    }
    public void setPoster_firstName(String poster_firstName) {
        this.poster_firstName = poster_firstName;
    }
    public String getPoster_lastName() {
        return poster_lastName;
    }
    public void setPoster_lastName(String poster_lastName) {
        this.poster_lastName = poster_lastName;
    }
    public String getPoster_image() {
        return poster_image;
    }
    public void setPoster_image(String poster_image) {
        this.poster_image = poster_image;
    }
    public User extractUser() {
        return new User(poster_firstName, poster_lastName, posterAlias, poster_image);
    }
    public Status extractStatus() {
        return new Status(post, extractUser(), timestamp, urls, mentions);
    }
}
