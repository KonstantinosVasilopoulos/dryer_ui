package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import com.aueb.idry.R;

public class ContainerGuideFirstActivity extends AdvancedAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_guide_first);

        Button returnBtn = findViewById(R.id.container_guide_first_return_btn);
        Button nextBtn = findViewById(R.id.container_guide_first_next_btn);

        returnBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ContainerGuideFirstActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ContainerGuideFirstActivity.this, ContainerGuideSecondActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Play audio message containing instructions
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (preference.getVoiceInstructions()) {
                speak(getString(R.string.tts_first_container), TextToSpeech.QUEUE_FLUSH, null, "tts_first_container");
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
            final Button nextBtn = findViewById(R.id.container_guide_first_next_btn);
            nextBtn.performClick();
        } else if (stringArrayContains(words, "go") || stringArrayContains(words, "back")) {
            // Click on the previous button
            final Button finalBtn = findViewById(R.id.container_guide_first_return_btn);
            finalBtn.performClick();
        }
    }
}