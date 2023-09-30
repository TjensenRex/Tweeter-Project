package edu.byu.cs.tweeter.client.presenter;

import android.content.Context;
import edu.byu.cs.tweeter.client.cache.Cache;

public class StatusDialogPresenter {
    public void setObserver(Context context) {
        observer = (Observer) context;
    }

    public void postStatus(String post) {
        observer.onStatusPosted(post);
    }

    public interface Observer {
        void onStatusPosted(String post);
    }
    public interface View {

    }
    private View view;
    private Observer observer;
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
}
