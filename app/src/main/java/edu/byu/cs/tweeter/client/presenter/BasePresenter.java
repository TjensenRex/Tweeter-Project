package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.viewInterface.BaseView;

public class BasePresenter {
    private final BaseView view;
    public BaseView getView() {
        return view;
    }
    BasePresenter(BaseView view) {
        this.view = view;
    }
    public void displayMessage(String message) {
        view.displayMessage(message);
    }
}
