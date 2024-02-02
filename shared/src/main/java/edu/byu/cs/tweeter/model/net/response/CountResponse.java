package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class CountResponse extends Response {
    private int count;
    public CountResponse(boolean success) {
        super(success);
    }
    public CountResponse(boolean success, String message) {
        super(success, message);
    }
    public CountResponse(int count) {
        super(true);
        this.count = count;
    }
    public int getCount() {
        return count;
    }
}
