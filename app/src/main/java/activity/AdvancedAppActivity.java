package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import model.Preference;
import model.PreferenceDAO;
import utils.DryerListener;

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

    // Speech recognition
    protected SpeechRecognizer speech;
    private Intent recognizerIntent;
    private static final int SPEECH_REQUEST_CODE = 0;

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

        // Check if the speech recording permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            final String[] permissions = {Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(this, permissions, SPEECH_REQUEST_CODE);
        }

        // Initialize speech recognizer
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speech.setRecognitionListener(new DryerListener());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start listening for commands
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        speech.startListening(recognizerIntent);
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

        // Restart the voice listener
        speech.startListening(recognizerIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (preference.getVoiceInstructions()) {
            tts.stop();
            tts.shutdown();
        }

        // Stop listening for voice commands
        speech.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        speech.destroy();
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