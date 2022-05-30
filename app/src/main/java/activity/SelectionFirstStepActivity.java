package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;
import com.aueb.idry.T8816WP.DryingLevel;

import model.Routine;
import model.RoutineDAO;
import utils.SelectionBarStep;

public class SelectionFirstStepActivity extends AppCompatActivity {

    private DryingLevel selectedDryingLevel;
    private Button extraDryBtn;
    private Button normalBtn;
    private Button handIronBtn;
    private Button machineIronBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_first_step);

        // Check for a provided routine name
        Bundle params = getIntent().getExtras();
        String routineName;
        Routine newRoutine;
        RoutineDAO routines = RoutineDAO.getInstance(getApplicationContext());
        boolean editMode;
        if (params != null) {
            // Retrieve the routine
            routineName = params.getString("routine_name");
            newRoutine = routines.getRoutine(routineName);
            editMode = true;

        } else {
            // Create new routine
            final int routinesSize = routines.getRoutines().size() + 1;
            routineName = "Routine " + String.valueOf(routinesSize);
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
        extraDryBtn = (Button) findViewById(R.id.dryingLevelExtraDryBtn);
        normalBtn = (Button) findViewById(R.id.dryingLevelNormalBtn);
        handIronBtn = (Button) findViewById(R.id.dryingLevelHandIronBtn);
        machineIronBtn = (Button) findViewById(R.id.dryingLevelMachineIronBtn);

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
        extraDryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDryingLevelBtnClicked(DryingLevel.EXTRA_DRY);
            }
        });

        normalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDryingLevelBtnClicked(DryingLevel.NORMAL);
            }
        });

        handIronBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDryingLevelBtnClicked(DryingLevel.HAND_IRON);
            }
        });

        machineIronBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDryingLevelBtnClicked(DryingLevel.MACHINE_IRON);
            }
        });

        // Apply a listener to the arrow buttons
        final Button upArrowBtn = (Button) findViewById(R.id.dryingLevelUpArrowBtn);
        final Button downArrowBtn = (Button) findViewById(R.id.dryingLevelDownArrowBtn);
        upArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        downArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        // Add listeners to the button's controlling the routine creation/selection flow
        final Button previousBtn = (Button) findViewById(R.id.dryingLevelPreviousBtn);
        final Button nextBtn = (Button) findViewById(R.id.dryingLevelNextBtn);
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete unsaved routine
                if (!editMode) {
                    routines.removeRoutine(routineName);
                }

                // Return to the routines' menu activity
                Intent intent = new Intent(SelectionFirstStepActivity.this, RoutineMenuActivity.class);
                startActivity(intent);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update routine if the drying level was altered
                if (!selectedDryingLevel.equals(newRoutine.getLevel())) {
                    newRoutine.setLevel(selectedDryingLevel);
                    routines.updateRoutine(newRoutine);
                }

                // Move to the activity which selects the new routine's programme
                Intent intent = new Intent(SelectionFirstStepActivity.this, SelectionSecondStepActivity.class);
                intent.putExtra("routine_name", routineName);
                startActivity(intent);
            }
        });
    }

    public void onDryingLevelBtnClicked(DryingLevel level) {
        switch (level) {
            case EXTRA_DRY:
                selectDryingLevelBtn(extraDryBtn);
                break;

            case NORMAL:
                selectDryingLevelBtn(normalBtn);
                break;

            case HAND_IRON:
                selectDryingLevelBtn(handIronBtn);
                break;

            case MACHINE_IRON:
                selectDryingLevelBtn(machineIronBtn);
                break;
        }

        selectedDryingLevel = level;
    }

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
}