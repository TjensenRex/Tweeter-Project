package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.observer.PostObserver;

public class PostStatusHandler extends Handler {
    private PostObserver observer;

    public PostStatusHandler(PostObserver observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
        if (success) {
            observer.success();
        } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}
