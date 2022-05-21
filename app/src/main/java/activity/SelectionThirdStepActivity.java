package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.Routine;
import model.RoutineDAO;
import utils.SelectionBarStep;

public class SelectionThirdStepActivity extends AppCompatActivity {

    private Date date;
    private Button dateBtn;
    private static final String DATE_PATTERN = "EEEE dd MMMM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_third_step);

        // Retrieve the routine using the provided parameters
        Bundle params = getIntent().getExtras();
        String routineName = params.getString("routine_name");
        Routine routine = RoutineDAO.getInstance().getRoutine(routineName);

        // Create the selection bar
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putString("current_step", SelectionBarStep.TIME.name());
        args.putString("routine_name", routineName);
        SelectionBarFragment selectionBarFragment = new SelectionBarFragment();
        selectionBarFragment.setArguments(args);
        transaction.add(R.id.timeSelectionBar, selectionBarFragment);
        transaction.commit();

        // Get the date
        if (routine.getDelay() != 0L) {
            // Routine's date
            date = new Date(routine.getDelay());
        } else {
            // Current time
            date = Calendar.getInstance().getTime();
        }

        // Set date button's label
        dateBtn = (Button) findViewById(R.id.timeSelectDateBtn);
        final Locale locale = getResources().getConfiguration().locale;
        final SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN, locale);
        dateBtn.setText(format.format(date));

        // Add listeners to left & right arrow buttons for controlling the date
        Button dateLeftBtn = (Button) findViewById(R.id.timeDateLeftBtn);
        Button dateRightBtn = (Button) findViewById(R.id.timeDateRightBtn);

        // Left arrow button
        dateLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Roll back one day
                Calendar calendar = Calendar.getInstance(locale);
                calendar.setTime(date);
                calendar.add(Calendar.DATE, -1); // 1 day
                date = calendar.getTime();
                Log.d("routine_date_right", date.toString());

                // Display new date
                displayDate();
            }
        });

        // Right arrow button
        dateRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add one day
                Calendar calendar = Calendar.getInstance(locale);
                calendar.setTime(date);
                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                Log.d("routine_date_right", date.toString());

                // Display new date
                displayDate();
            }
        });

        // Previous & next buttons
        Button previousBtn = (Button) findViewById(R.id.timePreviousBtn);
        Button nextBtn = (Button) findViewById(R.id.timeNextBtn);

        // Set listener for going back to the second step activity
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionThirdStepActivity.this, SelectionSecondStepActivity.class);
                intent.putExtra("routine_name", routineName);
                startActivity(intent);
            }
        });

        // Add listener for going to the routine preview activity
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve date's hours and minutes from the time-picker fragment
                Log.d("time_next_btn", "clicked");
                TimePickerFragment timePicker = (TimePickerFragment) getSupportFragmentManager().findFragmentById(R.id.timeTimePicker);
                if (timePicker != null) {
                    Calendar calendar = Calendar.getInstance(locale);
                    calendar.setTime(date);
                    calendar.set(Calendar.HOUR, timePicker.getHours());
                    calendar.set(Calendar.MINUTE, timePicker.getMinutes());
                    calendar.set(Calendar.SECOND, 0);
                    date = calendar.getTime();

                    // Check that the given date is valid
                    Date now = Calendar.getInstance(locale).getTime();
                    if (date.compareTo(now) <= 0) {
                        // Display error notification
                        Snackbar.make(view, R.string.time_error_notification, Snackbar.LENGTH_SHORT).show();
                        return; // Exit function since the date is invalid
                    }

                    // Save the routine's new date
                    long delay = Math.abs(now.getTime() - date.getTime());
                    routine.setDelay(delay);
                    RoutineDAO.getInstance(getApplicationContext()).updateRoutine(routine);

                    // Navigate to the routine preview activity
                    Intent intent = new Intent(SelectionThirdStepActivity.this, ProgramOverviewActivity.class);
                    intent.putExtra("routine_name", routineName);
                    startActivity(intent);
                }
            }
        });

        // Set listener for the start now button
        Button nowBtn = (Button) findViewById(R.id.timeNowBtn);
        nowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set routine delay to zero
                routine.setDelay(0L);
                RoutineDAO.getInstance(getApplicationContext()).updateRoutine(routine);

                // Start routine preview activity
                Intent intent = new Intent(SelectionThirdStepActivity.this, ProgramOverviewActivity.class);
                intent.putExtra("routine_name", routineName);
                startActivity(intent);
            }
        });
    }

    // Helper method
    // Change displayed date
    private void displayDate() {
        // Create date formatter
        Locale locale = getResources().getConfiguration().locale;
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN, locale);
        String dateStr = format.format(date);

        // Resize date string if it's too big
        float textSize = 16.f;
        if (dateStr.length() > 12)
            textSize = 14.f;
        dateBtn.setTextSize(textSize);

        dateBtn.setText(dateStr);
    }
}