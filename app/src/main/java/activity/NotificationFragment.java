package activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aueb.idry.R;

import utils.Notifications;

public class NotificationFragment extends Fragment {

    private static final String NOTIFICATION = "notification";

    private Notifications notification;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(Notifications notification) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable(NOTIFICATION, notification);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notification = (Notifications) getArguments().getSerializable(NOTIFICATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the label's text and the listener for navigating to the relevant guide activity
        TextView notificationLabel = view.findViewById(R.id.notificationLabel);
        switch (notification) {
            case FILTERS:
            default:
                notificationLabel.setText(R.string.notification_filters);
                notificationLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Start filters' guide activity
                        Intent intent = new Intent(getActivity(), FilterGuideFirstActivity.class);
                        startActivity(intent);
                    }
                });
                break;

            case CONTAINERS:
                notificationLabel.setText(R.string.notification_container);
                notificationLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: Start container's guide activity
//                        Intent intent = new Intent(getActivity(), );
//                        startActivity(intent);
                    }
                });
                break;
        }
    }
}