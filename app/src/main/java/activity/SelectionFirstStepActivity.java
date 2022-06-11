package activity;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import com.aueb.idry.R;
import com.aueb.idry.T8816WP.DryingLevel;

import model.Routine;
import model.RoutineDAO;
import utils.SelectionBarStep;

public class SelectionFirstStepActivity extends AdvancedAppActivity {

    private DryingLevel selectedDryingLevel;
    private Button extraDryBtn;
    private Button normalBtn;
    private Button handIronBtn;
    private Button machineIronBtn;
    private String routineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_first_step);

        // Check for a provided routine name
        Bundle params = getIntent().getExtras();
        Routine newRoutine;
        RoutineDAO routines = RoutineDAO.getInstance(getApplicationContext());
        boolean editMode;
        if (params != null) {
            // Retrieve the routine
            routineName = params.getString("routine_name");
            setRoutineActivityExtras(routineName);
            newRoutine = routines.getRoutine(routineName);

            // Check for a provided edit mode
            editMode = params.getBoolean("edit_mode", true);

        } else {
            // Create new routine
            final int routinesSize = routines.getRoutines().size() + 1;
            routineName = "Routine " + routinesSize;
            newRoutine = new Routine(routineName);
            routines.saveRoutine(newRoutine);
            editMode = false;
        }

        // Create the selection bar
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putString("current_step", SelectionBarStep.DRYING_LEVEL.name());
        args.putString("routine_name", routineName);
        SelectionBarFragment selectionBarFragment = new SelectionBarFragment();
        selectionBarFragment.setArguments(args);
        transaction.add(R.id.dryingLevelSelectionBar, selectionBarFragment);
        transaction.commit();

        // Find all drying level buttons
        selectedDryingLevel = DryingLevel.NORMAL;
        extraDryBtn = findViewById(R.id.dryingLevelExtraDryBtn);
        normalBtn = findViewById(R.id.dryingLevelNormalBtn);
        handIronBtn = findViewById(R.id.dryingLevelHandIronBtn);
        machineIronBtn = findViewById(R.id.dryingLevelMachineIronBtn);

        // Highlight the selected drying level
        // Normal is the preset
        switch (newRoutine.getLevel()) {
            case EXTRA_DRY:
                selectDryingLevelBtn(extraDryBtn);
                break;

            case NORMAL:
            default:
                selectDryingLevelBtn(normalBtn);
                break;

            case HAND_IRON:
                selectDryingLevelBtn(handIronBtn);
                break;

            case MACHINE_IRON:
                selectDryingLevelBtn(machineIronBtn);
                break;
        }

        // Create and set a listener for every drying level button
        extraDryBtn.setOnClickListener(v -> onDryingLevelBtnClicked(DryingLevel.EXTRA_DRY));

        normalBtn.setOnClickListener(v -> onDryingLevelBtnClicked(DryingLevel.NORMAL));

        handIronBtn.setOnClickListener(v -> onDryingLevelBtnClicked(DryingLevel.HAND_IRON));

        machineIronBtn.setOnClickListener(v -> onDryingLevelBtnClicked(DryingLevel.MACHINE_IRON));

        // Apply a listener to the arrow buttons
        final Button upArrowBtn = findViewById(R.id.dryingLevelUpArrowBtn);
        final Button downArrowBtn = findViewById(R.id.dryingLevelDownArrowBtn);
        upArrowBtn.setOnClickListener(v -> {
            // Switch to the previous drying level
            switch (selectedDryingLevel) {
                case EXTRA_DRY:
                    selectDryingLevelBtn(machineIronBtn);
                    selectedDryingLevel = DryingLevel.MACHINE_IRON;
                    break;

                case NORMAL:
                    selectDryingLevelBtn(extraDryBtn);
                    selectedDryingLevel = DryingLevel.EXTRA_DRY;
                    break;

                case HAND_IRON:
                    selectDryingLevelBtn(normalBtn);
                    selectedDryingLevel = DryingLevel.NORMAL;
                    break;

                case MACHINE_IRON:
                    selectDryingLevelBtn(handIronBtn);
                    selectedDryingLevel = DryingLevel.HAND_IRON;
                    break;
            }
        });

        downArrowBtn.setOnClickListener(v -> {
            // Switch to the next drying level (going down)
            switch (selectedDryingLevel) {
                case EXTRA_DRY:
                    selectDryingLevelBtn(normalBtn);
                    selectedDryingLevel = DryingLevel.NORMAL;
                    break;

                case NORMAL:
                    selectDryingLevelBtn(handIronBtn);
                    selectedDryingLevel = DryingLevel.HAND_IRON;
                    break;

                case HAND_IRON:
                    selectDryingLevelBtn(machineIronBtn);
                    selectedDryingLevel = DryingLevel.MACHINE_IRON;
                    break;

                case MACHINE_IRON:
                    selectDryingLevelBtn(extraDryBtn);
                    selectedDryingLevel = DryingLevel.EXTRA_DRY;
                    break;
            }
        });

        // Add listeners to the button's controlling the routine creation/selection flow
        final Button previousBtn = findViewById(R.id.dryingLevelPreviousBtn);
        final Button nextBtn = findViewById(R.id.dryingLevelNextBtn);
        previousBtn.setOnClickListener(v -> {
            // Delete unsaved routine
            if (!editMode) {
                routines.removeRoutine(routineName);
            }

            // Return to the routines' menu activity
            Intent intent = new Intent(SelectionFirstStepActivity.this, RoutineMenuActivity.class);
            startActivity(intent);
        });

        nextBtn.setOnClickListener(v -> {
            // Update routine if the drying level was altered
            if (!selectedDryingLevel.equals(newRoutine.getLevel())) {
                newRoutine.setLevel(selectedDryingLevel);
                routines.updateRoutine(newRoutine);
            }

            // Move to the activity which selects the new routine's programme
            Intent intent = new Intent(SelectionFirstStepActivity.this, SelectionSecondStepActivity.class);
            intent.putExtra("routine_name", routineName);
            intent.putExtra("edit_mode", editMode);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (preference.getVoiceInstructions()) {
            // Wait for 1 seconds before speaking
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                // Prompt the use to select a drying level
                String toSpeak = getString(R.string.tts_first_step_prompt);
                speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_first_step_prompt");
            }, 1000);
        }
    }

    public void onDryingLevelBtnClicked(DryingLevel level) {
        String toSpeak;
        switch (level) {
            case EXTRA_DRY:
                selectDryingLevelBtn(extraDryBtn);
                toSpeak = getString(R.string.tts_extra_dry_selected);
                break;

            case NORMAL:
            default:
                selectDryingLevelBtn(normalBtn);
                toSpeak = getString(R.string.tts_normal_selected);
                break;

            case HAND_IRON:
                selectDryingLevelBtn(handIronBtn);
                toSpeak = getString(R.string.tts_hand_iron_selected);
                break;

            case MACHINE_IRON:
                selectDryingLevelBtn(machineIronBtn);
                toSpeak = getString(R.string.tts_machine_iron_selected);
                break;
        }

        selectedDryingLevel = level;

        // Inform the user about the selected drying level via the microphone
        if (preference.getVoiceInstructions()) {
            speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_drying_level_selected");
        }
    }

    @Override
    public void listenerUpdated(String match) {
        super.listenerUpdated(match);

        // Navigate activities using voice commands
        String[] words = match.split(" ");
        if (stringArrayContains(words, "proceed")) {
            // Click on the next button
            final Button nextBtn = findViewById(R.id.dryingLevelNextBtn);
            nextBtn.performClick();
        } else if (stringArrayContains(words, "go") || stringArrayContains(words, "back")) {
            // Click on the previous button
            final Button finalBtn = findViewById(R.id.dryingLevelPreviousBtn);
            finalBtn.performClick();
        }

        // Choose drying level via voice commands
        if (stringArrayContains(words, "extra") && stringArrayContains(words, "dry")) {
            extraDryBtn.performClick();
        } else if (stringArrayContains(words, "normal")) {
            normalBtn.performClick();
        } else if (stringArrayContains(words, "hand")) {
            handIronBtn.performClick();
        } else if (stringArrayContains(words, "machine")) {
            machineIronBtn.performClick();
        }
    }

    // Helper method
    // Apply stylistic changes to the selected drying level button
    private void selectDryingLevelBtn(Button btn) {
        // De-highlight the previously selected button
        ColorStateList btnColor = AppCompatResources.getColorStateList(this, R.color.gray_300);
        switch (selectedDryingLevel) {
            case EXTRA_DRY:
                extraDryBtn.setBackgroundTintList(btnColor);
                break;

            case NORMAL:
                normalBtn.setBackgroundTintList(btnColor);
                break;

            case HAND_IRON:
                handIronBtn.setBackgroundTintList(btnColor);
                break;

            case MACHINE_IRON:
                machineIronBtn.setBackgroundTintList(btnColor);
                break;
        }

        // Highlight the button
        btn.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.gray_400));
    }

    @Override
    public void onBackPressed() {
        // Click on the previous button
        final Button previousBtn = findViewById(R.id.dryingLevelPreviousBtn);
        previousBtn.performClick();
    }
}