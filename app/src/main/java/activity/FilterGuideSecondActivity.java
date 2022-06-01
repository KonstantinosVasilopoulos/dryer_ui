package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import com.aueb.idry.R;

public class FilterGuideSecondActivity extends AdvancedAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_guide_second);

        Button backBtn = (Button) findViewById(R.id.filter_guide_second_back_btn);
        Button nextBtn = (Button) findViewById(R.id.filter_guide_second_next_btn);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FilterGuideSecondActivity.this, FilterGuideFirstActivity.class);
            startActivity(intent);
        });

        nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FilterGuideSecondActivity.this, FilterGuideThirdActivity.class);
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
                tts.speak(getString(R.string.tts_second_filter), TextToSpeech.QUEUE_FLUSH, null, "tts_second_filter");
            }
        }, 1000);
    }
}