package edu.byu.cs.tweeter.client.presenter;

import android.content.Context;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.viewInterface.BaseView;

public class StatusDialogPresenter extends BasePresenter {
    public interface StatusDialogueObserver {
        void onStatusPosted(String post);
    }
    public interface View extends BaseView { // kept to match the pattern in the others
    }
    private StatusDialogueObserver statusObserver;
    public StatusDialogPresenter(BaseView v) {
        super(v);
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
        statusObserver = (StatusDialogueObserver) context;
    }
    public void postStatus(String post) {
        statusObserver.onStatusPosted(post);
    }
}
