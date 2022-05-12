package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.Routine;
import model.RoutineDAO;
import utils.SelectionBarStep;

public class SelectionThirdStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_third_step);

        // Create the selection bar
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putString("current_step", SelectionBarStep.TIME.name());
        SelectionBarFragment selectionBarFragment = new SelectionBarFragment();
        selectionBarFragment.setArguments(args);
        transaction.add(R.id.timeSelectionBar, selectionBarFragment);
        transaction.commit();

        // Get the routine from the provided parameters
        Bundle params = getIntent().getExtras();
        String routineName = params.getString("routine_name");
        Routine routine = RoutineDAO.getInstance().getRoutine(routineName);

        // Get the date
        Date date;
        if (routine.getDelay() != 0L) {
            // Routine's date
            date = new Date(routine.getDelay());
        } else {
            // Now
            date = Calendar.getInstance().getTime();
        }

        // Set date button's label
        Locale locale = getResources().getConfiguration().locale;
        SimpleDateFormat format = new SimpleDateFormat("EEEE dd MMMM", locale);

        // Set time button's label
        format = new SimpleDateFormat("kk:mm", locale);
    }
}