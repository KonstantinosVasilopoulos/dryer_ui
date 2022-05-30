package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aueb.idry.R;
import com.aueb.idry.T8816WP.TumbleDryer;
import com.aueb.idry.T8816WP.TumbleDryerImp;

import java.util.Locale;

import utils.Notifications;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private final int NOTIFICATIONS_LAYOUT = R.id.mainNotificationsScrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Resize the start button's text if it's too big
        final int TEXT_GROW_THRESHOLD = 10;
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

        initTextToSpeech();

        // Create notification fragments if required
        // Filters notification
        if (dryer.checkFilters()) {
            addNotificationFragment(Notifications.FILTERS);
        }

        // Container notification
        if (dryer.checkContainer()) {
            addNotificationFragment(Notifications.CONTAINERS);
        }

        // Set listener for start button
        mainStartBtn.setOnClickListener(view -> {
            // Turn on the dryer
            if (!dryer.getPowerStatus()) {
                dryer.turnOn();

                // Use text-to-speech to inform the user
                String toSpeak = getString(R.string.tts_turn_on);
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_main");
            }

            // Unlock the dryer's door
            if (dryer.isClosed()) {
                dryer.openDoor();
            }

            // Wait for the text-to-speech to finish talking
            boolean isSpeaking = tts.isSpeaking();
            while (isSpeaking) {
                isSpeaking = tts.isSpeaking();
            }

            // Start the routine menu activity
            //Intent intent = new Intent(MainActivity.this, RoutineMenuActivity.class);
            Intent intent = new Intent(MainActivity.this, DoorGuideActivity.class);
            intent.putExtra("className", "RoutineMenuActivity");
            startActivity(intent);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        initTextToSpeech();
    }

    @Override
    protected void onStop() {
        super.onStop();

        tts.stop();
        tts.shutdown();
    }

    // Helper methods
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

    // Initialize the text-to-speech component
    private void initTextToSpeech() {
        tts = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                // Check language availability
                Locale locale = getResources().getConfiguration().locale;
                if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                    tts.setLanguage(locale);
                } else {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }
}