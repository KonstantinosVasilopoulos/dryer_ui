package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import com.aueb.idry.R;

public class FilterGuideThirdActivity extends AdvancedAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_guide_third);

        Button backBtn = (Button) findViewById(R.id.filter_guide_third_back_btn);
        Button nextBtn = (Button) findViewById(R.id.filter_guide_third_next_btn);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FilterGuideThirdActivity.this, FilterGuideSecondActivity.class);
            startActivity(intent);
        });

        nextBtn.setOnClickListener(view -> {
            Intent intent = new Intent(FilterGuideThirdActivity.this, FilterGuideFourthActivity.class);
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
                speak(getString(R.string.tts_third_filter), TextToSpeech.QUEUE_FLUSH, null, "tts_third_filter");
            }
        }, 1000);
    }
}