package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;

public class StatusesResponse extends PagedResponse {
    private List<Status> story;
    public StatusesResponse(boolean success, boolean hasMorePages) {
        super(success, hasMorePages);
    }
    public StatusesResponse(boolean success, String message, boolean hasMorePages) {
        super(success, message, hasMorePages);
    }
    public StatusesResponse(List<Status> story, boolean hasMorePages) {
        super(true, hasMorePages);
        this.story = story;
    }
    public List<Status> getStory() {
        return story;
    }
    public void setStory(List<Status> story) {
        this.story = story;
    }
}
