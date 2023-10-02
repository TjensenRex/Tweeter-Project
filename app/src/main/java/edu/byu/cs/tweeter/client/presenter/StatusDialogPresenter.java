package edu.byu.cs.tweeter.client.presenter;

import android.content.Context;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public class StatusDialogPresenter {
    public interface StatusDialogueObserver extends ServiceObserver {
        void onStatusPosted(String post);
    }
    public interface View {

    }
    private View view;
    private StatusDialogueObserver observer;
    public StatusDialogPresenter(View v) {
        view = v;
    }
    public String getAlias() {
        return Cache.getInstance().getCurrUser().getAlias();
    }
    public String getName() {
        return Cache.getInstance().getCurrUser().getName();
    }
    public String getImageUrl() {
        return Cache.getInstance().getCurrUser().getImageUrl();
    }
    public void setObserver(Context context) {
        observer = (StatusDialogueObserver) context;
    }
    public void postStatus(String post) {
        observer.onStatusPosted(post);
    }
}
