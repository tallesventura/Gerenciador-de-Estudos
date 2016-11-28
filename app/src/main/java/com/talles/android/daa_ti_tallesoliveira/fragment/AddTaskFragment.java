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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talles.android.daa_ti_tallesoliveira.R;
import com.talles.android.daa_ti_tallesoliveira.model.TaskModel;

import java.util.HashMap;

public class AddTaskFragment extends Fragment {

    private Spinner spinnerDaysWeek;
    private Spinner spinnerTaskTypes;
    private TextView txtFrom;
    private TextView txtTo;
    private Button btnConfirm;
    private EditText txtDescription;
    private EditText txtSubject;
    private EditText txtEmail;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference tasksRef = null;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public AddTaskFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null) {
            DatabaseReference rootRef = database.getReference("users");
            DatabaseReference userRef = rootRef.child(user.getUid());
            tasksRef = userRef.child("tasks");
        }

        tasksRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TaskModel tm = dataSnapshot.getValue(TaskModel.class);
                if(tm != null){
                    Toast.makeText(getActivity(),"Atividade adicionada.",Toast.LENGTH_LONG);
                }else{
                    Toast.makeText(getActivity(),"Erro ao adicionar atividade.",Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        txtDescription = (EditText) view.findViewById(R.id.add_task_description);
        txtSubject = (EditText) view.findViewById(R.id.add_task_subject);
        txtEmail = (EditText) view.findViewById(R.id.add_task_supervisorEmail);

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

        this.btnConfirm = (Button) view.findViewById(R.id.add_task_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
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

    private void addTask(){
        String day = spinnerDaysWeek.getSelectedItem().toString();
        String start_time = txtFrom.getText().toString();
        String end_time = txtTo.getText().toString();
        String type = spinnerTaskTypes.getSelectedItem().toString();
        String subject = txtSubject.getText().toString();
        String description = txtDescription.getText().toString();
        String email = txtEmail.getText().toString();

        TaskModel tm = new TaskModel(day,start_time,end_time,type,subject,description,email);

        int priority;
        switch (day){
            case "Segunda":
                priority = 1;
                break;
            case "Terça":
                priority = 2;
                break;
            case "Quarta":
                priority = 3;
                break;
            case "Quinta":
                priority = 4;
                break;
            case "Sexta":
                priority = 5;
                break;
            case "Sábado":
                priority = 6;
                break;
            case "Domingo":
                priority = 7;
                break;
            default:
                priority = 8;
        }

        String key = tasksRef.push().getKey();
        tm.setId(key);
        tasksRef.child(key).setValue(tm,priority);

    }
}
