package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aueb.idry.R;

import utils.SelectionBarStep;

public class SelectionBarFragment extends Fragment {

    private static final String CURRENT_STEP = "current_step";
    private static final String ROUTINE_NAME = "routine_name";

    private SelectionBarStep currentStep;
    private String routineName;

    public SelectionBarFragment() {
        // Required empty public constructor
    }

    public static SelectionBarFragment newInstance(SelectionBarStep step, String routineName) {
        SelectionBarFragment fragment = new SelectionBarFragment();
        Bundle args = new Bundle();

        // Put the string of the current step
        args.putString(CURRENT_STEP, step.name());
        args.putString(ROUTINE_NAME, routineName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String currentStepString = getArguments().getString(CURRENT_STEP);
            currentStep = SelectionBarStep.valueOf(currentStepString);

            // Extract the routine's name
            routineName = getArguments().getString(ROUTINE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selection_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the current step's color to a more vivid one
        TextView stepText;
        switch (currentStep) {
            case DRYING_LEVEL:
            default:
                stepText =  (TextView) view.findViewById(R.id.barDryingLevel);
                break;

            case PROGRAMME:
                stepText =  (TextView) view.findViewById(R.id.barProgramme);
                break;

            case TIME:
                stepText =  (TextView) view.findViewById(R.id.barTime);
                break;

            case PREVIEW:
                stepText =  (TextView) view.findViewById(R.id.barPreview);
                break;
        }

        stepText.setTextColor(getResources().getColor(R.color.confirmation_green));

        // Make each section of the bar clickable
        // Drying level
        TextView barDryingLevel = (TextView) view.findViewById(R.id.barDryingLevel);
        barDryingLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do not restart the current activity
                if (!currentStep.equals(SelectionBarStep.DRYING_LEVEL)) {
                    // Change activity to the first step activity
                    Intent intent = new Intent(getActivity(), SelectionFirstStepActivity.class);
                    intent.putExtra(ROUTINE_NAME, routineName);
                    startActivity(intent);
                }
            }
        });

        // Programme
        TextView barProgramme = (TextView) view.findViewById(R.id.barProgramme);
        barProgramme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The user shouldn't be able to move forward using this bar
                if (!currentStep.equals(SelectionBarStep.PROGRAMME) && !currentStep.equals(SelectionBarStep.DRYING_LEVEL)) {
                    Intent intent = new Intent(getActivity(), SelectionSecondStepActivity.class);
                    intent.putExtra(ROUTINE_NAME, routineName);
                    startActivity(intent);
                }
            }
        });

        // Time
        TextView barTime = (TextView) view.findViewById(R.id.barTime);
        barTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only preview step can navigate back to time step
                if (currentStep.equals(SelectionBarStep.PREVIEW)) {
                    Intent intent = new Intent(getActivity(), SelectionThirdStepActivity.class);
                    intent.putExtra(ROUTINE_NAME, routineName);
                    startActivity(intent);
                }
            }
        });
    }

    public SelectionBarStep getCurrentStep() {
        return currentStep;
    }
}