package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthenticatedTask {

    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose items are being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private final User targetUser;

    /**
     * Maximum number of statuses to return (i.e., page size).
     */

    private final int limit;

    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private final T lastItem;

    public List<T> getItems() {
        return items;
    }

    /**
     * The items returned in the current page of results.
     */
    private List<T> items;

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    /**
     * Indicates whether there are more pages of items that can be retrieved on subsequent calls.
     */
    private boolean hasMorePages;

    protected PagedTask(AuthToken authToken, User targetUser, int limit, T lastItem, Handler messageHandler) {
        super(authToken, messageHandler);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }
    public void setItems(List<T> items) {
        this.items = items;
    }
    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    protected User getTargetUser() {
        return targetUser;
    }

    protected int getLimit() {
        return limit;
    }

    protected T getLastItem() {
        return lastItem;
    }

    @Override
    protected void runTask() {
        Pair<List<T>, Boolean> pageOfItems = queryItems();

        items = pageOfItems.getFirst();
        hasMorePages = pageOfItems.getSecond();

        // Call sendSuccessMessage if successful
        sendSuccessMessage();
        // or call sendFailedMessage if not successful
        // sendFailedMessage()
    }

    protected Pair<List<T>, Boolean> queryItems() {
        return new Pair<>(items, hasMorePages);
    }

    protected abstract List<User> getUsersForItems(List<T> items);

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }
}
