package com.talles.android.daa_ti_tallesoliveira.fragment;

/**
 * Created by talles on 9/25/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.talles.android.daa_ti_tallesoliveira.R;
import com.talles.android.daa_ti_tallesoliveira.model.TaskModel;

public class ViewTaskFragment extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference tasksRef = null;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Spinner spinnerDaysWeek;
    private Spinner spinnerTaskTypes;
    private TextView txtFrom;
    private TextView txtTo;
    private EditText txtSubject;
    private EditText txtDescription;
    private EditText txtEmail;

    private String id;

    public ViewTaskFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null) {
            DatabaseReference rootRef = database.getReference();
            DatabaseReference userRef = rootRef.child("users").child(user.getUid());
            tasksRef = userRef.child("tasks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_task, container, false);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_view_task, menu);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtFrom = (TextView) view.findViewById(R.id.view_task_EditFromTime);
        txtTo = (TextView) view.findViewById(R.id.view_task_EditToTime);


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

        Bundle bundle = this.getArguments();
        if(bundle != null){
            id = bundle.getString("id");
            populateView();
        }else{
            Toast.makeText(getContext(),"Erro ao carregar atividade",Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void populateView(){

        Query query = tasksRef.orderByKey().equalTo(id).limitToFirst(1);

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Carregando atividade");
        pd.setCancelable(false);
        pd.show();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TaskModel tm = dataSnapshot.getValue(TaskModel.class);

                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Erro ao carregar atividade",Toast.LENGTH_LONG);
                pd.dismiss();
            }
        });
    }
}
