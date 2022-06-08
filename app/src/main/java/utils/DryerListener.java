package utils;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;
import java.util.List;

import activity.AdvancedAppActivity;

/**
 * Recognition listener implementation for receiving user voice commands
 *
 * @see android.speech.RecognitionListener
 */
public class DryerListener implements RecognitionListener {

    private final AdvancedAppActivity parentActivity;
    private final List<DryerListenerObserver> observers;
    private static final String VOICE_KEYWORD = "dryer";

    /**
     * Default constructor receiving the parent activity as the only parameter.
     *
     * @param parentActivity the listener's parent activity
     */
    public DryerListener(AdvancedAppActivity parentActivity) {
        this.parentActivity = parentActivity;
        this.observers = new ArrayList<>();
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {
        parentActivity.restartListener();
    }

    @Override
    public void onResults(Bundle bundle) {
        List<String> matches  = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null) {
            // Search for the keyword
            for (String match : matches) {
                if (match.startsWith(VOICE_KEYWORD)) {
                    // Have the observers handle the output
                    notifyObservers(matches);
                    break;
                }
            }

            // Restart the speech recognition
            parentActivity.restartListener();
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    /**
     * Add a new observer to be notified when the listener has new input.
     *
     * @param observer new observer
     */
    public void addObserver(DryerListenerObserver observer) {
        observers.add(observer);
    }

    // Helper method
    private void notifyObservers(List<String> matches) {
        for (DryerListenerObserver observer : observers) {
            observer.listenerUpdated(matches);
        }
    }
}
