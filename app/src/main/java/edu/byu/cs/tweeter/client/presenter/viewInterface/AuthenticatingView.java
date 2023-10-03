package edu.byu.cs.tweeter.client.presenter.viewInterface;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticatingView extends Fragment implements BaseView {
    protected Toast activityToast;
    protected EditText alias;
    protected EditText password;
    public void startViewActivity(User user, String name) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
        activityToast.cancel();
        Toast.makeText(getContext(), "Hello " + name, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
