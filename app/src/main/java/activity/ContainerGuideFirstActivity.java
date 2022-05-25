package activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;

public class ContainerGuideFirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_guide_first);

        Button returnBtn = (Button) findViewById(R.id.container_guide_first_return_btn);
        Button nextBtn = (Button) findViewById(R.id.container_guide_first_next_btn);

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContainerGuideFirstActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContainerGuideFirstActivity.this, ContainerGuideSecondActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}