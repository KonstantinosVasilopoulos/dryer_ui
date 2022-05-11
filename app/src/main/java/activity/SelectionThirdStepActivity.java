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

        // Date & time buttons
        Button dateBtn = (Button) findViewById(R.id.timeSelectDateBtn);
        Button timeBtn = (Button) findViewById(R.id.timeSelectTimeBtn);

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
        dateBtn.setText(format.format(date));

        // Set time button's label
        format = new SimpleDateFormat("kk:mm", locale);
        timeBtn.setText(format.format(date));

        // TODO: Open calendar on date button click

        // Open time-picker widget on time button click
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(locale);
                calendar.setTime(date);
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTitleText(R.string.time_select_time_label)
                        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                        .setMinute(calendar.get(Calendar.MINUTE))
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        // Use the simple version of the time-picker
                        .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                        .build();
                timePicker.show(getSupportFragmentManager(), "SelectionThirdStepActivity");
            }
        });
    }
}