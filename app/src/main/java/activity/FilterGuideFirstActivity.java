package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;

public class FilterGuideFirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_guide_first);

        Button returnBtn = (Button) findViewById(R.id.filter_guide_first_return_btn);
        Button nextBtn = (Button) findViewById(R.id.filter_guide_first_next_btn);

        // Go back to the main activity
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterGuideFirstActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterGuideFirstActivity.this, FilterGuideSecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}