package activity;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import com.aueb.idry.R;
import com.aueb.idry.T8816WP.Programme;

import java.util.HashMap;
import java.util.Map;

import model.Routine;
import model.RoutineDAO;
import utils.SelectionBarStep;

public class SelectionSecondStepActivity extends AdvancedAppActivity {

    private Programme selectedProgramme;
    private Map<Integer, Programme> programmeBtnIds;
    private String routineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_second_step);

        // Make a dictionary containing all the buttons' ids and their enum value
        programmeBtnIds = new HashMap<>();
        programmeBtnIds.put(R.id.programmeCottonsBtn, Programme.COTTONS);
        programmeBtnIds.put(R.id.programmeMinimumIronBtn, Programme.MINIMUM_IRON);
        programmeBtnIds.put(R.id.programmeWoollensBtn, Programme.WOOLLENS);
        programmeBtnIds.put(R.id.programmeOuterwearBtn, Programme.OUTERWEAR);
        programmeBtnIds.put(R.id.programmeProofingBtn, Programme.PROOFING);
        programmeBtnIds.put(R.id.programmeExpressBtn, Programme.EXPRESS);
        programmeBtnIds.put(R.id.programmeAutomaticPlusBtn, Programme.AUTOMATIC_PLUS);
        programmeBtnIds.put(R.id.programmeShirtsBtn, Programme.SHIRTS);
        programmeBtnIds.put(R.id.programmeDenimBtn, Programme.DENIM);
        programmeBtnIds.put(R.id.programmeHygieneBtn, Programme.HYGIENE);
        programmeBtnIds.put(R.id.programmeWarmAirBtn, Programme.WARM_AIR);
        programmeBtnIds.put(R.id.programmeGentleSmoothingBtn, Programme.GENTLE_SMOOTHING);

        // Get the routine from the provided parameters
        Bundle params = getIntent().getExtras();
        routineName = params.getString("routine_name");
        setRoutineActivityExtras(routineName);
        Routine routine = RoutineDAO.getInstance(this).getRoutine(routineName);
        selectedProgramme = routine.getProgramme();

        // Get the edit mode from the parameters
        boolean editMode = params.getBoolean("edit_mode", true);

        // Create the selection bar
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putString("current_step", SelectionBarStep.PROGRAMME.name());
        args.putString("routine_name", routineName);
        SelectionBarFragment selectionBarFragment = new SelectionBarFragment();
        selectionBarFragment.setArguments(args);
        transaction.add(R.id.programmeSelectionBar, selectionBarFragment);
        transaction.commit();

        // Find the selected programme's button
        Button selectedBtn;
        for (int programmeBtnId : programmeBtnIds.keySet()) {
            if (selectedProgramme.equals(programmeBtnIds.get(programmeBtnId))) {
                selectedBtn = findViewById(programmeBtnId);

                // Highlight the button
                selectedBtn.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.gray_400));
                break;
            }
        }

        // Add a listener to every programme button
        Button programmeBtn;
        for (int programmeBtnId : programmeBtnIds.keySet()) {
            programmeBtn = findViewById(programmeBtnId);
            programmeBtn.setOnClickListener(view -> selectProgramme(programmeBtnId));
        }

        // Set listeners for up and down arrow keys
        Button upArrowBtn = findViewById(R.id.programmeUpArrowBtn);
        upArrowBtn.setOnClickListener(view -> {
            switch (selectedProgramme) {
                case COTTONS:
                default:
                    selectProgramme(R.id.programmeGentleSmoothingBtn);
                    break;

                case MINIMUM_IRON:
                    selectProgramme(R.id.programmeCottonsBtn);
                    break;

                case WOOLLENS:
                    selectProgramme(R.id.programmeMinimumIronBtn);
                    break;

                case OUTERWEAR:
                    selectProgramme(R.id.programmeWoollensBtn);
                    break;

                case PROOFING:
                    selectProgramme(R.id.programmeOuterwearBtn);
                    break;

                case EXPRESS:
                    selectProgramme(R.id.programmeProofingBtn);
                    break;

                case AUTOMATIC_PLUS:
                    selectProgramme(R.id.programmeExpressBtn);
                    break;

                case SHIRTS:
                    selectProgramme(R.id.programmeAutomaticPlusBtn);
                    break;

                case DENIM:
                    selectProgramme(R.id.programmeShirtsBtn);
                    break;

                case HYGIENE:
                    selectProgramme(R.id.programmeDenimBtn);
                    break;

                case WARM_AIR:
                    selectProgramme(R.id.programmeHygieneBtn);
                    break;

                case GENTLE_SMOOTHING:
                    selectProgramme(R.id.programmeWarmAirBtn);
                    break;
            }
        });

        // Down arrow
        Button downArrowBtn = findViewById(R.id.programmeDownArrowBtn);
        downArrowBtn.setOnClickListener(view -> {
            switch (selectedProgramme) {
                case COTTONS:
                default:
                    selectProgramme(R.id.programmeMinimumIronBtn);
                    break;

                case MINIMUM_IRON:
                    selectProgramme(R.id.programmeWoollensBtn);
                    break;

                case WOOLLENS:
                    selectProgramme(R.id.programmeOuterwearBtn);
                    break;

                case OUTERWEAR:
                    selectProgramme(R.id.programmeProofingBtn);
                    break;

                case PROOFING:
                    selectProgramme(R.id.programmeExpressBtn);
                    break;

                case EXPRESS:
                    selectProgramme(R.id.programmeAutomaticPlusBtn);
                    break;

                case AUTOMATIC_PLUS:
                    selectProgramme(R.id.programmeShirtsBtn);
                    break;

                case SHIRTS:
                    selectProgramme(R.id.programmeDenimBtn);
                    break;

                case DENIM:
                    selectProgramme(R.id.programmeHygieneBtn);
                    break;

                case HYGIENE:
                    selectProgramme(R.id.programmeWarmAirBtn);
                    break;

                case WARM_AIR:
                    selectProgramme(R.id.programmeGentleSmoothingBtn);
                    break;

                case GENTLE_SMOOTHING:
                    selectProgramme(R.id.programmeCottonsBtn);
                    break;
            }
        });

        // Set listeners for previous and next buttons
        Button previousBtn = findViewById(R.id.programmePreviousBtn);
        previousBtn.setOnClickListener(view -> {
            // Go back to the first selection activity
            Intent intent = new Intent(SelectionSecondStepActivity.this, SelectionFirstStepActivity.class);
            intent.putExtra("routine_name", routineName);
            intent.putExtra("edit_mode", editMode);
            startActivity(intent);
        });

        Button nextBtn = findViewById(R.id.programmeNextBtn);
        nextBtn.setOnClickListener(view -> {
            // Save routine if it's content was edited
            if (!selectedProgramme.equals(routine.getProgramme())) {
                routine.setProgramme(selectedProgramme);
                RoutineDAO.getInstance(getApplicationContext()).updateRoutine(routine);
            }

            // Navigate to the time/delay selection activity
            Intent intent = new Intent(SelectionSecondStepActivity.this, SelectionThirdStepActivity.class);
            intent.putExtra("routine_name", routineName);
            intent.putExtra("edit_mode", editMode);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Play prompt
        if (preference.getVoiceInstructions()) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                final String toSpeak = getString(R.string.tts_second_step_prompt);
                speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "tts_second_step_prompt");
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

        // Select programme using voice commands
        for (int btnId : programmeBtnIds.keySet()) {
            // Retrieve the programme's name as a string
            String programmeStr = getString(programmeBtnIds.get(btnId).getStringId()).toLowerCase();
            if (stringArrayContains(words, programmeStr)) {
                // Click on the programme's button
                Button programmeBtn = findViewById(btnId);
                programmeBtn.performClick();
            }
        }
    }

    // Helper method
    // React to a programme button click by highlighting it
    private void selectProgramme(int btnId) {
        if (programmeBtnIds != null) {
            // De-highlight the previously selected button
            for (int programmeBtnId : programmeBtnIds.keySet()) {
                if (selectedProgramme.equals(programmeBtnIds.get(programmeBtnId))) {
                    Button previousBtn = findViewById(programmeBtnId);
                    previousBtn.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.gray_300));
                    break;
                }
            }

            // Save the newly selected programme
            selectedProgramme = programmeBtnIds.get(btnId);

            // Highlight the selected button
            Button btn = findViewById(btnId);
            btn.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.gray_400));
        }
    }

    @Override
    public void onBackPressed() {
        // Click on the previous button
        final Button previousBtn = findViewById(R.id.programmePreviousBtn);
        previousBtn.performClick();
    }
}