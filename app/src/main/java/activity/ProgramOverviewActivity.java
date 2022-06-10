package activity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aueb.idry.R;
import com.aueb.idry.T8816WP.TumbleDryerImp;

import model.Routine;
import model.RoutineDAO;
import utils.SelectionBarStep;

public class ProgramOverviewActivity extends AdvancedAppActivity {
    // Routine basic variables initialization
    private String routineName;
    private long duration;
    private Routine routine;
    private TumbleDryerImp tumbleDryerImp = TumbleDryerImp.getInstance();

    // ActivityStatus variable corresponds to the confirm/resume and stop phase.
    // false: confirm/resume phase, true: stop phase
    // This helps us to understand if we are able to start the duration count down timer.
    private boolean activityStatus = false;
    private boolean durationStarted = false;
    private boolean savePreference = true;
    private boolean editMode;

    // Textviews initialization
    private TextView programDuration;
    private TextView dryerLevel;
    private TextView clothesProgram;
    private TextView delayTime;
    private TextView dryerStatus;
    private TextView dryerStatusDetails;

    // Buttons initialization
    private Button previousBtn;
    private Button confirmResumeBtn;
    private Button stopBtn;
    private Button returnToHomeBtn;

    // LinearLayouts initialization
    private LinearLayout durationLayout;
    private LinearLayout programCompletedLayout;
    private LinearLayout delayLayout;
    private LinearLayout statusLayout;
    private LinearLayout removeClothesNotificationLayout;

    // Once the confirm Button is clicked, it will change to resume Button (text change).
    private boolean confirmClicked = false;

    // CountDownTimers initialization
    private MyCountDownTimer programDurationCountDown;
    private MyCountDownTimer delayTimeCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_overview);

        // Textviews declaration
        programDuration = (TextView) findViewById(R.id.programme_overview_duration_txt);
        dryerLevel = (TextView) findViewById(R.id.program_overview_dryer_level_txt);
        clothesProgram = (TextView) findViewById(R.id.program_overview_clothes_programme_txt);
        delayTime = (TextView) findViewById(R.id.program_overview_delay_time_txt);
        dryerStatus = (TextView) findViewById(R.id.program_overview_status_txt);
        dryerStatusDetails = (TextView) findViewById(R.id.program_overview_status_details_txt);

        // Buttons declaration
        previousBtn = (Button) findViewById(R.id.overview_previous_btn);
        confirmResumeBtn = (Button) findViewById(R.id.overview_confirm_btn);
        stopBtn = (Button) findViewById(R.id.overview_stop_btn);
        returnToHomeBtn = (Button) findViewById(R.id.overview_return_to_home_page_btn);

        // LinearLayout declaration
        durationLayout = (LinearLayout) findViewById(R.id.program_overview_durationLayout);
        programCompletedLayout = (LinearLayout) findViewById(R.id.program_overview_completedSuccessfullyLayout);
        delayLayout = (LinearLayout) findViewById(R.id.program_overview_delayLayout);
        statusLayout = (LinearLayout) findViewById(R.id.program_overview_statusLayout);
        removeClothesNotificationLayout = (LinearLayout) findViewById(R.id.program_overview_status_details_removeClothesLayout);

        // Show the first Dialog Interface about saving the routine
        showSaveDialogInterface();

        // Retrieve the routine using the provided parameters
        Bundle params = getIntent().getExtras();
        routineName = params.getString("routine_name");
        routine = RoutineDAO.getInstance().getRoutine(routineName);
        setRoutineActivityExtras(routine.getName());

        // Get the edit mode from the parameters
        editMode = params.getBoolean("edit_mode", true);

        // Create the selection bar
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putString("current_step", SelectionBarStep.PREVIEW.name());
        args.putString("routine_name", routineName);
        SelectionBarFragment selectionBarFragment = new SelectionBarFragment();
        selectionBarFragment.setArguments(args);
        transaction.add(R.id.programmeOverviewBar, selectionBarFragment);
        transaction.commit();

        // Calculate the routine's duration
        duration = tumbleDryerImp.calculateDuration(routine.getLevel(), routine.getProgramme());

        // Create the count down timers
        programDurationCountDown = new MyCountDownTimer(duration, programDuration, 1);
        delayTimeCountDown = new MyCountDownTimer(routine.getDelay(), delayTime, 2);

        // For testing reasons
        //programDurationCountDown = new MyCountDownTimer(5000, programDuration, 1);
        //delayTimeCountDown = new MyCountDownTimer(6000, delayTime, 2);

        delayTimeCountDown.onStart();
        programDuration.setText(getFormatSimple(duration));

        // For testing reasons
        //dryerLevel.setText(getString(DryingLevel.NORMAL.getStringId()));
        //clothesProgram.setText(getString(Programme.DENIM.getStringId()));

        dryerLevel.setText(getString(routine.getLevel().getStringId()));
        clothesProgram.setText(routine.getProgramme().getStringId());

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectionThirdStepActivity.class);
                intent.putExtra("routine_name", routineName);
                intent.putExtra("edit_mode", editMode);
                startActivity(intent);
            }
        });

        confirmResumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityStatus = true;
                if (!confirmClicked) {
                    confirmResumeBtn.setText(R.string.resume_btn);
                    dryerStatusDetails.setText(R.string.program_overview_status_details_resume_txt);
                    confirmClicked = true;
                }
                else if (programDurationCountDown.hasTimerStarted()) {
                    programDurationCountDown.onResume();
                }

                if (delayTimeCountDown.hasTimerFinished() && !durationStarted) {
                    programDurationCountDown.onStart();
                    durationStarted = true;
                }

                previousBtn.setVisibility(View.GONE);
                confirmResumeBtn.setVisibility(View.GONE);
                stopBtn.setVisibility(View.VISIBLE);
                dryerStatus.setText(R.string.program_overview_status_working);
                dryerStatusDetails.setVisibility(View.GONE);

            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityStatus = false;
                stopBtn.setVisibility(View.GONE);
                previousBtn.setVisibility(View.VISIBLE);
                confirmResumeBtn.setVisibility(View.VISIBLE);
                dryerStatus.setText(R.string.program_overview_status_suspended);
                dryerStatusDetails.setVisibility(View.VISIBLE);
                programDurationCountDown.onPause();
            }
        });

        returnToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RoutineMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Display the home button
        diplayHomeBtn(R.id.previewFunctionBtns);
    }

    private void onFinishDelayCountDownTimer() {
        delayLayout.setVisibility(View.GONE);
        statusLayout.setVisibility(View.VISIBLE);
        if (activityStatus) {
            programDurationCountDown.onStart();
        }
    }

    private void onFinishDurationCountDownTimer() {
        durationLayout.setVisibility(View.GONE);
        programCompletedLayout.setVisibility(View.VISIBLE);
        confirmResumeBtn.setVisibility(View.GONE);
        previousBtn.setVisibility(View.GONE);
        stopBtn.setVisibility(View.GONE);
        returnToHomeBtn.setVisibility(View.VISIBLE);
        statusLayout.setVisibility(View.GONE);
        removeClothesNotificationLayout.setVisibility(View.VISIBLE);

        if (!savePreference) {
            RoutineDAO.getInstance().removeRoutine(routineName);
        }
    }

    private void showSaveDialogInterface() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                            showRenameDialogInterface();
                        break;
                    case  DialogInterface.BUTTON_NEGATIVE:
                            readRoutineInfo();
                            Toast.makeText(ProgramOverviewActivity.this, getString(R.string.program_overview_notification_message_not_saved), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        // Have the text-to-speech instance ask the user
        if (preference.getVoiceInstructions()) {
            // Play after 1 seconds
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                final String toSpeak = getString(R.string.tts_preview_save_dialog);
                speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_preview_save_dialog");
            }, 500);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        builder.setTitle(R.string.program_overview_dialog_message_save_routine)
                .setPositiveButton(R.string.yes_label, dialogClickListener)
                .setNegativeButton(R.string.no_label, dialogClickListener).show();
    }

    private void showRenameDialogInterface() {
        final AlertDialog alertDialog;
        final EditText editText = new EditText(this);
        editText.setTextColor(ContextCompat.getColor(this, R.color.gray_100));
        editText.setText(routineName);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // Does nothing
                        break;
                    case  DialogInterface.BUTTON_NEGATIVE:
                        readRoutineInfo();
                        Toast.makeText(ProgramOverviewActivity.this, getString(R.string.program_overview_notification_message_not_saved), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        builder.setTitle(R.string.program_overview_dialog_message_rename_routine)
                .setView(editText)
                .setPositiveButton(R.string.confirm_label, dialogClickListener)
                .setNegativeButton(R.string.cancel_label, dialogClickListener);

        alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoutineDAO.getInstance().removeRoutine(routineName);
                String newRoutineName;
                newRoutineName = editText.getText().toString();
                if (newRoutineName.trim().equals("")) {

                    // Play audio error message
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        final String toSpeak = getString(R.string.tts_preview_empty_name);
                        speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_preview_empty_name");
                    }, 500);

                    Toast.makeText(ProgramOverviewActivity.this, getString(R.string.program_overview_notification_message_empty_text), Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view, R.string.program_overview_notification_message_empty_text, Snackbar.LENGTH_SHORT).show();
                }
                else if (!RoutineDAO.getInstance().containsName(newRoutineName) || (routineName.equals(newRoutineName))) {
                    routine.setName(newRoutineName);
                    routineName = newRoutineName;
                    RoutineDAO.getInstance().saveRoutine(routine);
                    Toast.makeText(ProgramOverviewActivity.this, getString(R.string.program_overview_notification_message_saved), Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view, R.string.program_overview_notification_message_saved, Snackbar.LENGTH_SHORT).show();
                    savePreference = true;

                    // Activate edit mode to not delete the routine if the user backtracks
                    editMode = true;

                    // Play audio message reading the routine's information and dismiss this dialog
                    readRoutineInfo();
                    alertDialog.dismiss();
                }
                else {
                    // Play audio error message
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        final String toSpeak = getString(R.string.tts_preview_different_name);
                        speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_preview_different_name");
                    }, 500);

                    Toast.makeText(ProgramOverviewActivity.this, getString(R.string.program_overview_notification_message_name_match), Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view, R.string.program_overview_notification_message_name_match, Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    // Read the user's choice out loud
    private void readRoutineInfo() {
        if (preference.getVoiceInstructions()) {
            // Play after 1 seconds
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                final int hours = (int) (duration / 3600000);
                final int minutes = ((int) (duration / 60000)) - hours * 60;
                final String levelStr = getString(routine.getLevel().getStringId());
                String programmeStr = getString(routine.getProgramme().getStringId());
                String toSpeak = getResources().getQuantityString(R.plurals.tts_preview_hours, hours, hours)
                        + " " + getResources().getQuantityString(R.plurals.tts_preview_minutes, minutes, minutes)
                        + " " + getString(R.string.tts_preview_level_programme, levelStr, programmeStr);
                speak(toSpeak, TextToSpeech.QUEUE_ADD, null, "tts_preview");
            }, 1000);
        }
    }

    private class MyCountDownTimer {
        private CountDownTimer countDownTimer;
        private final TextView textView;
        private long timeLeft;
        private final int formatType;
        private boolean timerStarted = false;
        private boolean timeFinished = false;

        public MyCountDownTimer(long duration, TextView textView, int formatType) {
            this.timeLeft = duration;
            this.countDownTimer = createNewCountDownTimer(duration);
            this.textView = textView;
            this.formatType = formatType;
        }

        public void onStart() {
            this.countDownTimer.start();
            this.timerStarted = true;
        }

        public void onPause() {
            this.countDownTimer.cancel();
        }

        public void onResume() {
            if (!this.hasTimerFinished()) {
                this.countDownTimer = createNewCountDownTimer(timeLeft);
                onStart();
            }
        }

        public CountDownTimer createNewCountDownTimer(long duration) {
            return new CountDownTimer(duration, 1000) {
                @Override
                public void onTick(long l) {
                    timeLeft = l;
                    updateText(l);
                }

                @Override
                public void onFinish() {
                    if (formatType == 1) {
                        onFinishDurationCountDownTimer();
                    }
                    else {
                        onFinishDelayCountDownTimer();
                    }
                    timeFinished = true;
                    textView.setText("Finished");
                }
            };
        }

        private void updateText(long l) {

            String format;

            if (formatType == 1) {
                format = getFormatSimple(l);
            }
            else {
                format = getFormatWithWords(l);
            }

            textView.setText(format);
        }

        public boolean hasTimerStarted() {
            return this.timerStarted;
        }

        public boolean hasTimerFinished() {
            return this.timeFinished;
        }
    }

    private String getFormatSimple(long l) {
        int seconds = (int) (l / 1000) % 60;
        int minutes = (int) ((l / (1000 * 60)) % 60);
        int hours = (int) ((l / (1000 * 60 * 60)) % 24);

        return getFormat(hours) + " : " + getFormat(minutes) + " : " + getFormat(seconds);
    }

    private String getFormatWithWords(long l) {
        int seconds = (int) (l / 1000) % 60;
        int minutes = (int) ((l / (1000 * 60)) % 60);
        int hours = (int) ((l / (1000 * 60 * 60)) % 24);
        int days = (int) (l / (1000 * 60 * 60 * 24));
        String format = "";

        if (days > 1) {
            format += days + " " + getString(R.string.days_label) + ", ";
        }
        else if (days == 1) {
            format += days + " " + getString(R.string.day_label) + ", ";
        }
        if (hours > 1 || (hours == 0 && days > 0)) {
            format += hours + " " + getString(R.string.hours_label) + ", ";
        }
        else if (hours == 1) {
            format += hours + " " + getString(R.string.hour_label) + ", ";
        }
        if (minutes > 1 || (minutes == 0 && (hours > 0 || days > 0))) {
            format += minutes + " " + getString(R.string.minutes_label) + " " + getString(R.string.and_label) + " ";
        }
        else if (minutes == 1) {
            format += minutes + " " + getString(R.string.minute_label) + " " + getString(R.string.and_label) + " ";
        }
        if (seconds == 1) {
            format += seconds + " " + getString(R.string.second_label);
        }
        else {
            format += seconds + " " + getString(R.string.seconds_label);
        }

        return format;
    }

    private String getFormat(int n) {
        if (n < 10) {
            return "0" + n;
        }
        return String.valueOf(n);
    }
}