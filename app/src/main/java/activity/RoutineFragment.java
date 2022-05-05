package activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aueb.idry.R;

import model.Routine;
import model.RoutineDAO;

public class RoutineFragment extends Fragment {

    private static final String ROUTINE_NAME = "routine_name";

    private Routine routine;

    public RoutineFragment() {
        // Required empty public constructor
    }

    public static RoutineFragment newInstance(Routine routine) {
        RoutineFragment fragment = new RoutineFragment();
        Bundle args = new Bundle();
        args.putString(ROUTINE_NAME, routine.getName());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Get the fragment's routine from the DAO using the name provided
            String routineName = getArguments().getString(ROUTINE_NAME);
            RoutineDAO routineDAO = RoutineDAO.getInstance(); // might be null!
            if (routineDAO != null) {
                routine = routineDAO.getRoutine(routineName);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the button's text
        Button routineSelectionBtn = (Button) view.findViewById(R.id.routineSelectionBtn);
        routineSelectionBtn.setText(routine.getName());
    }

    public Routine getRoutine() {
        return routine;
    }
}