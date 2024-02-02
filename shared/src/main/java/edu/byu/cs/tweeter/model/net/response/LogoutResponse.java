package edu.byu.cs.tweeter.model.net.response;

import com.ibm.jvm.Log;

public class LogoutResponse extends Response {
    public LogoutResponse(boolean success) {
        super(success);
    }
    public LogoutResponse(boolean success, String message) {
        super(success, message);
    }
}
