package edu.byu.cs.tweeter.client.presenter.viewInterface;

import android.content.Intent;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public abstract class PagedView<T> extends Fragment {
    public void displayMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
    public void startActivity(User user) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
        startActivity(intent);}
    public abstract void setLoadingFooter(boolean value);
    public abstract void addItems(List<T> statuses);
}
