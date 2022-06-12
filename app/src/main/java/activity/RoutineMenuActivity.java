package activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aueb.idry.R;

import java.util.Set;

import model.Routine;
import model.RoutineDAO;

public class RoutineMenuActivity extends AdvancedAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_menu);

        // Get the layout which will hold the routines' buttons
        LinearLayout routinesLayout = findViewById(R.id.routinesLayout);

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
        newRoutineBtn.setOnClickListener(view -> newRoutineBtnClicked());
    }

    @Override
    public void listenerUpdated(String match) {
        super.listenerUpdated(match);
        String[] words = match.split(" ");
        if (stringArrayContains(words, "new") && stringArrayContains(words, "routine")) {
            // Start the process for creating a new routine
            newRoutineBtnClicked();
        } else if (stringArrayContains(words, "start")) {
            String routineName = findRoutineNameInMatch(words);

            // Guard against non-existing routines
            if (routineName != null) {
                routineName = routineName.substring(0, 1).toUpperCase() + routineName.substring(1);
                RoutineDAO routineDAO = RoutineDAO.getInstance(this);
                if (routineDAO.containsName(routineName)) {
                    // Start the preview activity for the requested routine
                    Intent intent = new Intent(this, ProgramOverviewActivity.class);
                    intent.putExtra("routine_name", routineName);
                    intent.putExtra("edit_mode", true);
                    startActivity(intent);
                }
            }
        } else if (stringArrayContains(words, "edit")) {
            // Get the routine's name provided by the user
            String routineName = findRoutineNameInMatch(words);
            if (routineName != null) {
                routineName = routineName.substring(0, 1).toUpperCase() + routineName.substring(1);
                RoutineDAO routineDAO = RoutineDAO.getInstance(this);
                if (routineDAO.containsName(routineName)) {
                    // Start the first selection step to edit the routine
                    Intent intent = new Intent(this, SelectionFirstStepActivity.class);
                    intent.putExtra("routine_name", routineName);
                    startActivity(intent);
                }
            }
        }
    }

    private void newRoutineBtnClicked()  {
        // Start the first step activity for selecting drying level
        Intent intent = new Intent(RoutineMenuActivity.this, SelectionFirstStepActivity.class);
        startActivity(intent);
    }

    // Search in a voice command's output from a routine name
    private String findRoutineNameInMatch(String[] words) {
        for (int i = 0; i < words.length; i++) {
            if ((words[i].equalsIgnoreCase("start") ||
                    words[i].equalsIgnoreCase("delete")) ||
                    words[i].equalsIgnoreCase("edit") &&
                    i != words.length - 1) {

                // Return the rest of the words
                StringBuilder name = new StringBuilder();
                for (int j = i + 1; j < words.length; j++) {
                    name.append(words[j]);
                    name.append(" "); // words must be separated
                }

                return name.toString().trim();
            }
        }
        return null;
    }
}