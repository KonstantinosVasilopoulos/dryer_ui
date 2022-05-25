package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;

public class FilterGuideSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_guide_second);

        Button backBtn = (Button) findViewById(R.id.filter_guide_second_back_btn);
        Button nextBtn = (Button) findViewById(R.id.filter_guide_second_next_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterGuideSecondActivity.this, FilterGuideFirstActivity.class);
                startActivity(intent);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterGuideSecondActivity.this, FilterGuideThirdActivity.class);
                startActivity(intent);
            }
        });
    }
}