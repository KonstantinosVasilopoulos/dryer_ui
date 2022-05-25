package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;

public class FilterGuideFourthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_guide_fourth);

        Button backBtn = (Button) findViewById(R.id.filter_guide_fourth_back_btn);
        Button returnBtn = (Button) findViewById(R.id.filter_guide_fourth_return_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterGuideFourthActivity.this, FilterGuideThirdActivity.class);
                startActivity(intent);
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterGuideFourthActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}