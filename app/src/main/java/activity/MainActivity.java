package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aueb.idry.R;
import com.aueb.idry.T8816WP.TumbleDryer;
import com.aueb.idry.T8816WP.TumbleDryerImp;

import utils.Notifications;

public class MainActivity extends AppCompatActivity {
    private final int TEXT_GROW_THRESHOLD = 10;
    private final int NOTIFICATIONS_LAYOUT = R.id.mainNotificationsScrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Resize the start button's text if it's too big
        Button mainStartBtn = findViewById(R.id.mainStartBtnLabel);
        if (mainStartBtn.getText().length() < TEXT_GROW_THRESHOLD) {
            // Grow to 24sp
            mainStartBtn.setTextSize(24);
        }

        // Round the corners of the notifications' layout by allowing it to clip its corners
        LinearLayout notificationsLayout = findViewById(NOTIFICATIONS_LAYOUT);
        notificationsLayout.setClipToOutline(true);

        // Get the dryer interface
        TumbleDryer dryer = TumbleDryerImp.getInstance();

        // Create notification fragments if required
        // Filters notification
        if (dryer.checkFilters()) {
            addNotificationFragment(Notifications.FILTERS);
        }

        // Container notification
        if (dryer.checkContainer()) {
            addNotificationFragment(Notifications.CONTAINERS);
        }
    }

    // Helper method
    // Add a notification fragment
    private void addNotificationFragment(Notifications type) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable("notification", type);
        fragment.setArguments(args);
        transaction.add(NOTIFICATIONS_LAYOUT, fragment).commit();
    }
}