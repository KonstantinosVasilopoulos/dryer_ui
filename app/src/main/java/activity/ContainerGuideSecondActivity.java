package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;

public class ContainerGuideSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_guide_second);

        Button backBtn = (Button) findViewById(R.id.container_guide_second_back_btn);
        Button returnBtn = (Button) findViewById(R.id.container_guide_second_return_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContainerGuideSecondActivity.this, ContainerGuideFirstActivity.class);
                startActivity(intent);
                finish();
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContainerGuideSecondActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}