package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;

public class FilterGuideThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_guide_third);

        Button backBtn = (Button) findViewById(R.id.filter_guide_third_back_btn);
        Button nextBtn = (Button) findViewById(R.id.filter_guide_third_next_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterGuideThirdActivity.this, FilterGuideSecondActivity.class);
                startActivity(intent);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterGuideThirdActivity.this, FilterGuideFourthActivity.class);
                startActivity(intent);
            }
        });
    }
}