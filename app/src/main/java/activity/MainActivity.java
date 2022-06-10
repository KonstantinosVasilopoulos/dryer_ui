package activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.aueb.idry.R;
import com.aueb.idry.T8816WP.TumbleDryer;
import com.aueb.idry.T8816WP.TumbleDryerImp;

import pl.droidsonroids.gif.GifImageView;
import utils.Notifications;

public class MainActivity extends AdvancedAppActivity {
    private final int NOTIFICATIONS_LAYOUT = R.id.mainNotificationsScrollLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageHelper.setLocale(this, preference.getLanguageName());
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

        ImageButton notificationHelpBtn = findViewById(R.id.main_notification_help_btn);
        notificationHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelpDialog();
            }
        });

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

        // Set listener for start button
        mainStartBtn.setOnClickListener(view -> {
            // Turn on the dryer
            if (!dryer.getPowerStatus()) {
                dryer.turnOn();

                // Use text-to-speech to inform the user
                if (preference.getVoiceInstructions()) {
                    String toSpeak = getString(R.string.tts_turn_on);
                    speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_main");
                }
            }

            // Unlock the dryer's door
            if (dryer.isClosed()) {
                dryer.openDoor();
            }

            // Wait for the text-to-speech to finish talking
            if (preference.getVoiceInstructions()) {
                boolean isSpeaking = tts.isSpeaking();
                while (isSpeaking) {
                    isSpeaking = tts.isSpeaking();
                }
            }

            // Start the routine menu activity
            //Intent intent = new Intent(MainActivity.this, RoutineMenuActivity.class);
            Intent intent = new Intent(MainActivity.this, DoorGuideActivity.class);
            intent.putExtra("className", "RoutineMenuActivity");
            startActivity(intent);
        });
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

    private void showHelpDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.main_notification_help);

        if (!preference.getLanguage()) {
            GifImageView gifImg = dialog.findViewById(R.id.main_notification_help_gifImg);
            gifImg.setImageResource(R.drawable.notification_help_en);
        }

        Button dialogBtn = dialog.findViewById(R.id.main_notification_got_it_btn);
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}