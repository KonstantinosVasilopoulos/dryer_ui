package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import com.aueb.idry.R;

public class FilterGuideFourthActivity extends AdvancedAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_guide_fourth);

        Button backBtn = (Button) findViewById(R.id.filter_guide_fourth_back_btn);
        Button returnBtn = (Button) findViewById(R.id.filter_guide_fourth_return_btn);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FilterGuideFourthActivity.this, FilterGuideThirdActivity.class);
            startActivity(intent);
        });

        returnBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FilterGuideFourthActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Play audio message containing instructions
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (preference.getVoiceInstructions()) {
                speak(getString(R.string.tts_fourth_filter), TextToSpeech.QUEUE_FLUSH, null, "tts_fourth_filter");
            }
        }, 1000);
    }

    @Override
    public void listenerUpdated(String match) {
        super.listenerUpdated(match);

        // Navigate activities using voice commands
        String[] words = match.split(" ");
        if (stringArrayContains(words, "proceed")) {
            // Click on the next button
            final Button nextBtn = findViewById(R.id.filter_guide_fourth_back_btn);
            nextBtn.performClick();
        } else if (stringArrayContains(words, "go") || stringArrayContains(words, "back")) {
            // Click on the previous button
            final Button finalBtn = findViewById(R.id.filter_guide_fourth_return_btn);
            finalBtn.performClick();
        }
    }
}