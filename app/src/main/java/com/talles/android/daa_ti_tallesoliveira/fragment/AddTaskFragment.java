package com.talles.android.daa_ti_tallesoliveira.fragment;

/**
 * Created by talles on 9/25/16.
 */

import android.app.Activity;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.talles.android.daa_ti_tallesoliveira.R;

public class AddTaskFragment extends Fragment {

    private Spinner spinnerDaysWeek;
    private Spinner spinnerTaskTypes;
    private TextView txtFrom;
    private TextView txtTo;

    public AddTaskFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_add_task, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.spinnerDaysWeek = (Spinner) view.findViewById(R.id.days_week);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.array_days_of_the_week, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerDaysWeek.setAdapter(adapter);

        this.spinnerTaskTypes = (Spinner) view.findViewById(R.id.add_task_type);
        adapter = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.array_task_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTaskTypes.setAdapter(adapter);

        Calendar currentTime = Calendar.getInstance();
        final int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = currentTime.get(Calendar.MINUTE);

        final TimePickerDialog.OnTimeSetListener fromPickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                txtFrom.setText(i + ":" + i1);
            }
        };

        final TimePickerDialog.OnTimeSetListener toPickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                txtTo.setText(i + ":" + i1);
            }
        };

        txtFrom = (TextView) view.findViewById(R.id.add_task_EditFromTime);
        txtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePicker = new TimePickerDialog(view.getContext(), fromPickerListener,
                        hour, minute, true);
                timePicker.setTitle("Escolha um horário");
                timePicker.show();

            }
        });

        txtTo = (TextView) view.findViewById(R.id.add_task_EditFromTime);
        txtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePicker = new TimePickerDialog(view.getContext(), toPickerListener,
                        hour, minute, true);
                timePicker.setTitle("Escolha um horário");
                timePicker.show();

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}