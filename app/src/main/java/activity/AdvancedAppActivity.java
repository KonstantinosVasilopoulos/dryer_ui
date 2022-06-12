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

import com.aueb.idry.T8816WP.TumbleDryer;
import com.aueb.idry.T8816WP.TumbleDryerImp;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import model.Preference;
import model.PreferenceDAO;
import utils.DryerListener;

/**
 * Activity class containing more app-specific functionality
 * Included:
 *   - Preferences instance
 *   - Text-to-speech
 */
public abstract class AdvancedAppActivity extends AppCompatActivity {
    protected Preference preference;
    protected TextToSpeech tts;
    protected Map<String, Object> extras;

    // Speech recognition
    protected SpeechRecognizer speech;
    private Intent recognizerIntent;
    private static boolean voiceRecognitionAvailable;
    private static final int SPEECH_REQUEST_CODE = 0;

    private FunctionButtonsFragment functionButtons;

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
            DryerListener listener = new DryerListener(this);
            speech.setRecognitionListener(listener);
            voiceRecognitionAvailable = true;
        } else {
            voiceRecognitionAvailable = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Start listening for commands
        if (voiceRecognitionAvailable && preference.getVoiceCommands()) {
            recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
            speech.startListening(recognizerIntent);
        }
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
        restartListener();
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

    /**
     * Set extras dictionary in routine-related activities.
     */
    protected void setRoutineActivityExtras(String routineName) {
        extras.put("routine_name", routineName);
    }

    /**
     * Wrapper for TextToSpeech speak method.
     *
     * @param toSpeak string to be spoken
     * @param queueMode flush or add new string to the queue
     * @param params extra parameters
     * @param utteranceId a unique identifier
     */
    protected void speak(String toSpeak, int queueMode, Bundle params, String utteranceId) {
        tts.speak(toSpeak, queueMode, params, utteranceId); // Speak
    }

    /**
     * Start listening for voice commands again.
     */
    public void restartListener() {
        if (voiceRecognitionAvailable && preference.getVoiceCommands()) {
            speech.startListening(recognizerIntent);
        }
    }

    /**
     * Process the results of the listener.
     *
     * @param match the string the listener
     */
    public void listenerUpdated(String match) {
        // Skip if voice recognition is not available or the user has turned voice commands off
        if (!voiceRecognitionAvailable || !preference.getVoiceCommands()) {
            return;
        }

        // Search for a set of predefined commands
        String[] words = match.split(" ");
        if (stringArrayContains(words, "open") && stringArrayContains(words, "door")) {
            // Open the dryer's door & notify the function buttons' fragment about the change
            TumbleDryer dryer = TumbleDryerImp.getInstance();
            dryer.openDoor();
            if (functionButtons != null) {
                functionButtons.hideDoorUnlockBtn();
            }
        }
    }

    /**
     * Setter for the function buttons fragment.
     *
     * @param functionButtons the function buttons fragment of the activity
     */
    protected void setFunctionButtons(FunctionButtonsFragment functionButtons) {
        this.functionButtons = functionButtons;
    }

    /**
     * Search for an item in a string array. Ignore case.
     *
     * @param array the array to be searched
     * @param item the item to be found
     * @return true if the item was found, otherwise false
     */
    protected boolean stringArrayContains(String[] array, String item) {
        for (String word : array) {
            if (word.equalsIgnoreCase(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Display the home button in the function buttons fragment.
     */
    protected void displayHomeBtn() {
        if (functionButtons != null) {
            functionButtons.displayHomeBtn();
        }
    }
}