package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aueb.idry.R;
import com.aueb.idry.T8816WP.DryingLevel;

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

        // Create the selection bar
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle args = new Bundle();
        args.putString("current_step", SelectionBarStep.DRYING_LEVEL.name());
        SelectionBarFragment selectionBarFragment = new SelectionBarFragment();
        selectionBarFragment.setArguments(args);
        transaction.add(R.id.selectionBar, selectionBarFragment);
        transaction.commit();

        // Find all drying level buttons and select the Normal drying level
        selectedDryingLevel = DryingLevel.NORMAL;
        extraDryBtn = (Button) findViewById(R.id.dryingLevelExtraDryBtn);
        normalBtn = (Button) findViewById(R.id.dryingLevelNormalBtn);
        handIronBtn = (Button) findViewById(R.id.dryingLevelHandIronBtn);
        machineIronBtn = (Button) findViewById(R.id.dryingLevelMachineIronBtn);
        selectDryingLevelBtn(normalBtn);

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
                // Return to the routines' menu activity
                Intent intent = new Intent(SelectionFirstStepActivity.this, RoutineMenuActivity.class);
                startActivity(intent);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Move the activity which selects the new routine's programme
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
        ColorStateList btnColor = getResources().getColorStateList(R.color.gray_300);
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
        btn.setBackgroundTintList(getResources().getColorStateList(R.color.gray_400));
    }
}