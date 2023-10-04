package edu.byu.cs.tweeter.client.presenter.observer;

import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class UserObserver<T> extends ServiceObserver {
    public UserObserver(PagedPresenter<T> p, String verb) {
        super(p, verb);
    }
    public void startActivity(User user) {
        getPresenter().startActivity(user);
    }
    public PagedPresenter<T> getPresenter() {
        return (PagedPresenter<T>) super.getPresenter();
    }
}
