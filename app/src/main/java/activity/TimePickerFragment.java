package activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aueb.idry.R;

public class TimePickerFragment extends Fragment {

    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(String param1, String param2) {
        return new TimePickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hours & minutes input fields
        EditText hoursInput = (EditText) view.findViewById(R.id.timeHoursInput);
        EditText minutesInput = (EditText) view.findViewById(R.id.timeMinutesInput);

        // Set listeners ensuring that the user hasn't inserted any none-existing time
        // Hours
        hoursInput.addTextChangedListener(new TextWatcher() {
            private volatile boolean ignore = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!ignore) {
                    ignore = true;
                    String hoursStr = editable.toString();
                    try {
                        int hours = Integer.parseInt(hoursStr);

                        // The hours have to be between zero and twenty four
                        if (hours < 0 || hours > 23) {
                            setDoubleZero(editable);
                            ignore = false;
                            return;
                        } else if (hours > 0 && hours < 10) {
                            String hoursText = "0" + hoursStr;
                            editable.clear();
                            editable.append(hoursText);
                        }

                        // Make sure the text is exactly 2 digits in length
                        hoursStr = editable.toString();
                        if (hoursStr.length() > 2) {
                            String hoursText = hoursStr.substring(hoursStr.length() - 2);
                            editable.clear();
                            editable.append(hoursText);
                        }

                    } catch (NumberFormatException e) {
                        if (!hoursStr.isEmpty())
                            setDoubleZero(editable);
                    } finally {
                        ignore = false;
                    }
                }
            }
        });

        // Minutes
        minutesInput.addTextChangedListener(new TextWatcher() {
            private volatile boolean ignore = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Empty
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!ignore) {
                    ignore = true;
                    String minutesStr = editable.toString();
                    try {
                        int minutes = Integer.parseInt(minutesStr);

                        // The hours have to be between zero and twenty four
                        if (minutes < 0 || minutes > 59) {
                            setDoubleZero(editable);
                            ignore = false;
                            return;
                        } else if (minutes > 0 && minutes < 10) {
                            String minutesText = "0" + minutesStr;
                            editable.clear();
                            editable.append(minutesText);
                        }

                        // Make sure the text is exactly 2 digits in length
                        minutesStr = editable.toString();
                        if (minutesStr.length() > 2) {
                            String minutesText = minutesStr.substring(minutesStr.length() - 2);
                            editable.clear();
                            editable.append(minutesText);
                        }

                    } catch (NumberFormatException e) {
                        if (!minutesStr.isEmpty())
                            setDoubleZero(editable);
                    } finally {
                        ignore = false;
                    }
                }
            }
        });

        // Up & down buttons for controlling the hours and minutes
        Button hoursUpBtn = (Button) view.findViewById(R.id.timeHoursUpBtn);
        Button hoursDownBtn = (Button) view.findViewById(R.id.timeHoursDownBtn);
        Button minutesUpBtn = (Button) view.findViewById(R.id.timeMinutesUpBtn);
        Button minutesDownBtn = (Button) view.findViewById(R.id.timeMinutesDownBtn);

        // Set listener for the hours' up and down buttons
        hoursUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment hours by one
                Editable editable = hoursInput.getEditableText();
                String hoursStr = editable.toString();
                try {
                    int hours = Integer.parseInt(hoursStr);
                    hours++;
                    editable.clear();
                    editable.append(String.valueOf(hours));
                } catch (NumberFormatException e) {
                    // Empty
                }
            }
        });

        hoursDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Decrease hours by one
                Editable editable = hoursInput.getEditableText();
                String hoursStr = editable.toString();
                try {
                    int hours = Integer.parseInt(hoursStr);
                    hours--;

                    editable.clear();
                    if (hours < 0) {
                        // Circle back to 23
                        editable.append("23");
                    } else if (hours == 0) {
                        setDoubleZero(editable);
                    }  else {
                        editable.append(String.valueOf(hours));
                    }
                } catch (NumberFormatException e) {
                    // Empty
                }
            }
        });

        // Set listeners for the minutes' up and down buttons
        minutesUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Increment by one minute
                Editable editable = minutesInput.getEditableText();
                String minutesStr = editable.toString();
                try {
                    int minutes = Integer.parseInt(minutesStr);
                    minutes++;
                    editable.clear();
                    editable.append(String.valueOf(minutes));
                } catch (NumberFormatException e) {
                    // Empty
                }
            }
        });

        minutesDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Decrease minutes by one
                Editable editable = minutesInput.getEditableText();
                String minutesStr = editable.toString();
                try {
                    int minutes = Integer.parseInt(minutesStr);
                    minutes--;

                    editable.clear();
                    if (minutes < 0) {
                        // Circle back to 59 minutes
                        editable.append("59");
                    } else if (minutes == 0) {
                        setDoubleZero(editable);
                    } else {
                        editable.append(String.valueOf(minutes));
                    }
                } catch (NumberFormatException e) {
                    // Empty
                }
            }
        });
    }

    public int getHours() {
        // Get the hours input field and retrieve it's content
        return getTime(R.id.timeHoursInput);
    }

    public int getMinutes() {
        // Get the minutes field and extract it's value
        return getTime(R.id.timeMinutesInput);
    }

    // Helper methods
    // Set the text of an Editable instance to two zeroes
    private void setDoubleZero(Editable editable) {
        editable.clear();
        editable.append("00");
    }

    // Return the time from the provided EditText view
    private int getTime(int viewId) {
        EditText input = (EditText) getView().findViewById(viewId);
        String str = input.getEditableText().toString();
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}