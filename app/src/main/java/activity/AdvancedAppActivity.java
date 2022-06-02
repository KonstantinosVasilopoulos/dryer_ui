package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import model.Preference;
import model.PreferenceDAO;

/*
Activity class containing more app-specific functionality
Included:
    - Preferences instance
    - Text-to-speech
*/
public abstract class AdvancedAppActivity extends AppCompatActivity {
    protected Preference preference;
    protected TextToSpeech tts;
    protected Map<String, Object> extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get stored preferences
        preference = PreferenceDAO.getInstance(getApplicationContext()).retrievePreference();

        // Initialize text-to-speech if enabled
        if (preference.getVoiceInstructions()) {
            initTextToSpeech();
        }

        // Initialize dictionary that stores the activities extras
        extras = new HashMap<>();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (preference.getVoiceInstructions()) {
            initTextToSpeech();
        }

        // Restart activity in case of a language change
        finish();
        Intent intent = getIntent();
        for (String name : extras.keySet()) {
            // Cast extra to its required type
            switch (name) {
                case "routine_name":
                    intent.putExtra(name, (String) extras.get(name));
                    break;

                case "edit_mode":
                    intent.putExtra(name, (Boolean) extras.get(name));
                    break;

                default:
                    // Try to convert the extra into a string
                    try {
                        intent.putExtra(name, (String) extras.get(name));
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
            }
        }
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preference.getVoiceInstructions()) {
            tts.stop();
            tts.shutdown();
        }
    }

    // Initialize text-to-speech
    protected void initTextToSpeech() {
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

    // Set extras dictionary in routine-related activities
    protected void setRoutineActivityExtras(String routineName) {
        extras.put("routine_name", routineName);
    }
}