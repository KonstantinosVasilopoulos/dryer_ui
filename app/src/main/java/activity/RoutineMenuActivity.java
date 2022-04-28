package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
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
    }
}