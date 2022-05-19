package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aueb.idry.R;

public class MainActivity extends AppCompatActivity {
    private final int TEXT_GROW_THRESHOLD = 10;

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
        LinearLayout notificationsLayout = findViewById(R.id.mainNotificationsLayout);
        notificationsLayout.setClipToOutline(true);

        // TODO: Get the dryer interface

        // TODO: Create notification fragments if required
        // TODO: Filters notification

        // TODO: Container notification
    }
}