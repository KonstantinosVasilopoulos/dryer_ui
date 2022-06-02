package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.aueb.idry.R;

import java.util.Set;

import model.Routine;
import model.RoutineDAO;

public class RoutineMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_menu);

        // Get the layout which will hold the routines' buttons
        LinearLayout routinesLayout = (LinearLayout) findViewById(R.id.routinesLayout);

        // Add a selection button for every saved routine
        RoutineDAO routineDAO = RoutineDAO.getInstance(this);
        Set<Routine> routines = routineDAO.getRoutines();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        RoutineFragment fragment;
        for (Routine routine : routines) {
            // Create new fragment
            Bundle args = new Bundle();
            args.putString("routine_name", routine.getName());
            fragment = new RoutineFragment();
            fragment.setArguments(args);

            // Append fragment to transaction
            transaction.add(routinesLayout.getId(), fragment);
        }

        transaction.commit();

        // Resize button text if needed
        Button newRoutineBtn = findViewById(R.id.newRoutineBtn);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int screenWidth = displayMetrics.widthPixels;
        if (screenWidth < 74) {
            newRoutineBtn.setTextSize(14.f);
        }

        // Add listener to the button responsible for creating new routines
        newRoutineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the first step activity for selecting drying level
                Intent intent = new Intent(RoutineMenuActivity.this, SelectionFirstStepActivity.class);
                startActivity(intent);
            }
        });
    }
}