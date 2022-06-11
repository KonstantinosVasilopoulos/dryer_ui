package activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
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

public class SelectionThirdStepActivity extends AdvancedAppActivity {

    private volatile Date date;
    private Button dateBtn;
    private String routineName;
    private static final String DATE_PATTERN = "EEEE dd MMMM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_third_step);

        // Retrieve the routine using the provided parameters
        Bundle params = getIntent().getExtras();
        routineName = params.getString("routine_name");
        setRoutineActivityExtras(routineName);
        Routine routine = RoutineDAO.getInstance().getRoutine(routineName);

        // Get the edit mode from the parameters
        boolean editMode = params.getBoolean("edit_mode", true);

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
        final Locale locale = getResources().getConfiguration().locale;
        if (routine.getDelay() != 0L) {
            // Routine's date
            Calendar calendar = Calendar.getInstance(locale);
            final long routineTime = calendar.getTime().getTime() + routine.getDelay();
            calendar.setTimeInMillis(routineTime);
            date = calendar.getTime();

        } else {
            // Current time
            date = Calendar.getInstance(locale).getTime();
        }

        // Set date button's label
        dateBtn = findViewById(R.id.timeSelectDateBtn);
        displayDate();

        // Add listeners to left & right arrow buttons for controlling the date
        Button dateLeftBtn = findViewById(R.id.timeDateLeftBtn);
        Button dateRightBtn = findViewById(R.id.timeDateRightBtn);

        // Left arrow button
        dateLeftBtn.setOnClickListener(view -> {
            // Roll back one day
            Calendar calendar = Calendar.getInstance(locale);
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -1); // 1 day
            Calendar now = Calendar.getInstance(locale);
            if (calendar.get(Calendar.DAY_OF_YEAR) >= now.get(Calendar.DAY_OF_YEAR)) {
                date = calendar.getTime();
                Log.d("routine_date_left", date.toString());

                // Display new date
                displayDate();
            }
        });

        // Right arrow button
        dateRightBtn.setOnClickListener(view -> {
            // Add one day
            Calendar calendar = Calendar.getInstance(locale);
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            Log.d("routine_date_right", date.toString());

            // Display new date
            displayDate();
        });

        // Previous & next buttons
        Button previousBtn = findViewById(R.id.timePreviousBtn);
        Button nextBtn = findViewById(R.id.timeNextBtn);

        // Set listener for going back to the second step activity
        previousBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SelectionThirdStepActivity.this, SelectionSecondStepActivity.class);
            intent.putExtra("routine_name", routineName);
            intent.putExtra("edit_mode", editMode);
            startActivity(intent);
        });

        // Add listener for going to the routine preview activity
        nextBtn.setOnClickListener(view -> {
            // Retrieve date's hours and minutes from the time-picker fragment
            Log.d("time_next_btn", "clicked");
            TimePickerFragment timePicker = (TimePickerFragment) getSupportFragmentManager().findFragmentById(R.id.timeTimePicker);
            if (timePicker != null) {
                Calendar calendar = Calendar.getInstance(locale);
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHours());
                calendar.set(Calendar.MINUTE, timePicker.getMinutes());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                date = calendar.getTime();

                // Check that the given date is valid
                Date now = Calendar.getInstance(locale).getTime();
                if (date.compareTo(now) <= 0) {
                    // Display error notification
                    Snackbar.make(view, R.string.time_error_notification, Snackbar.LENGTH_SHORT).show();

                    // Play audio error message
                    if (preference.getVoiceInstructions()) {
                        String toSpeak = getString(R.string.tts_third_step_invalid_date);
                        speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_invalid_date");
                    }

                    return; // Exit the function since the date is invalid
                }

                // Save the routine's new date
                long delay = Math.abs(now.getTime() - date.getTime());
                routine.setDelay(delay);
                RoutineDAO.getInstance(getApplicationContext()).updateRoutine(routine);

                // Navigate to the routine preview activity
                Intent intent = new Intent(SelectionThirdStepActivity.this, ProgramOverviewActivity.class);
                intent.putExtra("routine_name", routineName);
                intent.putExtra("edit_mode", editMode);
                startActivity(intent);
            }
        });

        // Set listener for the start now button
        Button nowBtn = findViewById(R.id.timeNowBtn);
        nowBtn.setOnClickListener(view -> {
            // Set routine delay to zero
            routine.setDelay(0L);
            RoutineDAO.getInstance(getApplicationContext()).updateRoutine(routine);

            // Start routine preview activity
            Intent intent = new Intent(SelectionThirdStepActivity.this, ProgramOverviewActivity.class);
            intent.putExtra("routine_name", routineName);
            intent.putExtra("edit_mode", editMode);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Display the home button
        diplayHomeBtn(R.id.timeFunctionBtns);

        // Play prompt
        if (preference.getVoiceInstructions()) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                final String toSpeak = getString(R.string.tts_third_step_prompt);
                speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_third_step_prompt");
            }, 1000);
        }
    }

    @Override
    public void listenerUpdated(String match) {
        super.listenerUpdated(match);

        // Navigation commands
        String[] words = match.split(" ");
        if (stringArrayContains(words, "go") || stringArrayContains(words, "back")) {
            final Button previousBtn = findViewById(R.id.programmePreviousBtn);
            previousBtn.performClick();
        } else if (stringArrayContains(words, "proceed")) {
            final Button nextBtn = findViewById(R.id.programmeNextBtn);
            nextBtn.performClick();
        }

        // Start now voice command
        if (stringArrayContains(words, "start") || stringArrayContains(words, "now")) {
            final Button nowBtn = findViewById(R.id.timeNowBtn);
            nowBtn.performClick();
        }
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

    @Override
    public void onBackPressed() {
        // Click on the previous button
        final Button previousBtn = findViewById(R.id.timePreviousBtn);
        previousBtn.performClick();
    }
}