package com.talles.android.daa_ti_tallesoliveira.fragment;

/**
 * Created by talles on 9/25/16.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.talles.android.daa_ti_tallesoliveira.adapter.DayTaskListAdapter;
import com.talles.android.daa_ti_tallesoliveira.model.TaskModel;
import com.talles.android.daa_ti_tallesoliveira.utils.SubjectComparator;
import com.talles.android.daa_ti_tallesoliveira.utils.TaskTypeComparator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class DayFragment extends Fragment{

    private Spinner spinnerOrderBy;

    ArrayList<TaskModel> taskList;
    private DayTaskListAdapter adapter;
    private ListView listView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference tasksRef = null;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public DayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskList = new ArrayList();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null) {
            DatabaseReference rootRef = database.getReference();
            DatabaseReference userRef = rootRef.child("users").child(user.getUid());
            tasksRef = userRef.child("tasks");
        }

        taskList = new ArrayList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.fragment_day_listTasks);

        this.spinnerOrderBy = (Spinner) view.findViewById(R.id.fragment_day_spinner_orderBy);
        ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.array_orderBy_day, android.R.layout.simple_spinner_item);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerOrderBy.setAdapter(adap);

        initTaskList();

        spinnerOrderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        orderByTime();
                        break;
                    case 1:
                        orderBySubject();
                        break;
                    case 2:
                        orderByType();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                orderByTime();
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TaskModel tm = adapter.getItem(i);
                AlertDialog.Builder db = new AlertDialog.Builder(getContext());
                db.setTitle("Atenção");
                db.setMessage("Deseja realmente excluir a atividade?");

                db.setNegativeButton(
                        R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                db.setPositiveButton(
                        R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTask(tm);
                            }
                        });

                AlertDialog alert = db.create();
                alert.show();
                return false;
            }
        });

    }

    private void initTaskList(){

        adapter = new DayTaskListAdapter(getContext(), R.layout.day_list_item, taskList);
        listView.setAdapter(adapter);

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String key;
        switch (day){
            case Calendar.SUNDAY:
                key = "Domingo";
                break;
            case Calendar.MONDAY:
                key = "Segunda";
                break;
            case Calendar.TUESDAY:
                key = "Terça";
                break;
            case Calendar.WEDNESDAY:
                key = "Quarta";
                break;
            case Calendar.THURSDAY:
                key = "Quinta";
                break;
            case Calendar.FRIDAY:
                key = "Sexta";
                break;
            case Calendar.SATURDAY:
                key = "Sábado";
                break;
            default:
                key = null;
        }

        Query queryRef = tasksRef.orderByChild("day").equalTo(key);

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Carregando atividades");
        pd.setCancelable(false);
        pd.show();

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskList.clear();
                pd.dismiss();
                if(dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        TaskModel tm = ds.getValue(TaskModel.class);
                        taskList.add(tm);
                    }
                }else{
                    Toast.makeText(getContext(),"Nenhuma atividade registrada para hoje",Toast.LENGTH_LONG);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Falha ao carregar as atividades",Toast.LENGTH_LONG);
            }

        });

    }

    private void orderBySubject(){
        Collections.sort(taskList, new SubjectComparator());
        adapter.notifyDataSetChanged();
    }

    private void orderByType(){
        Collections.sort(taskList, new TaskTypeComparator());
        adapter.notifyDataSetChanged();
    }

    private void orderByTime(){
        initTaskList();
    }

    private void deleteTask(TaskModel tm){
        Query deleteQuery = tasksRef.orderByChild("id").equalTo(tm.getId());

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Deletando...");
        pd.setCancelable(false);
        pd.show();

        deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    TaskModel tm = snapshot.getValue(TaskModel.class);
                    taskList.remove(tm);
                    snapshot.getRef().removeValue();
                }
                pd.dismiss();
                Toast.makeText(getContext(),"Atividade deletada",Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
                Toast.makeText(getContext(),"Falha ao deletar",Toast.LENGTH_LONG);
            }
        });
    }

}
