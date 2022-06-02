package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import com.aueb.idry.R;

public class FilterGuideFirstActivity extends AdvancedAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_guide_first);

        Button returnBtn = (Button) findViewById(R.id.filter_guide_first_return_btn);
        Button nextBtn = (Button) findViewById(R.id.filter_guide_first_next_btn);

        // Go back to the main activity
        returnBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FilterGuideFirstActivity.this, MainActivity.class);
            startActivity(intent);
        });

        nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FilterGuideFirstActivity.this, FilterGuideSecondActivity.class);
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
                tts.speak(getString(R.string.tts_first_filter), TextToSpeech.QUEUE_FLUSH, null, "tts_first_filter");
            }
        }, 1000);
    }
}