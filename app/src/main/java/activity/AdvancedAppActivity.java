package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get stored preferences
        preference = PreferenceDAO.getInstance(getApplicationContext()).retrievePreference();

        // Initialize text-to-speech if enabled
        if (preference.getVoiceInstructions()) {
            initTextToSpeech();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (preference.getVoiceInstructions()) {
            initTextToSpeech();
        }
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
}