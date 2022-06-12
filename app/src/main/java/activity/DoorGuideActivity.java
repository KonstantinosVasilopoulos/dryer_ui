package activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.aueb.idry.R;

public class DoorGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_guide);

        Bundle arg = getIntent().getExtras();

        Class classToIntent = null;
        try {
            classToIntent = Class.forName("activity." + arg.getString("className"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Class finalClassToIntent = classToIntent;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(DoorGuideActivity.this, finalClassToIntent);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}