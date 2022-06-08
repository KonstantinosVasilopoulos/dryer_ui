package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import com.aueb.idry.R;

public class ContainerGuideSecondActivity extends AdvancedAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_guide_second);

        Button backBtn = findViewById(R.id.container_guide_second_back_btn);
        Button returnBtn = findViewById(R.id.container_guide_second_return_btn);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ContainerGuideSecondActivity.this, ContainerGuideFirstActivity.class);
            startActivity(intent);
            finish();
        });

        returnBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ContainerGuideSecondActivity.this, MainActivity.class);
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
                speak(getString(R.string.tts_second_container), TextToSpeech.QUEUE_FLUSH, null, "tts_second_container");
            }
        }, 1000);
    }
}