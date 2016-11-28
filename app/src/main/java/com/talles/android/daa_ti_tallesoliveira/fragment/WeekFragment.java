package com.talles.android.daa_ti_tallesoliveira.fragment;

/**
 * Created by talles on 9/25/16.
 */
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.talles.android.daa_ti_tallesoliveira.adapter.WeekTaskListAdapter;
import com.talles.android.daa_ti_tallesoliveira.model.TaskModel;
import com.talles.android.daa_ti_tallesoliveira.utils.SubjectComparator;
import com.talles.android.daa_ti_tallesoliveira.utils.TaskTypeComparator;

import java.util.ArrayList;
import java.util.Collections;


public class WeekFragment extends Fragment{

    private Spinner spinnerOrderBy;
    private ListView listView;

    ArrayList<TaskModel> taskList;
    private WeekTaskListAdapter adapter;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference tasksRef = null;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    //private FirebaseListAdapter<TaskModel> adapter;


    public WeekFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        return inflater.inflate(R.layout.fragment_week, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.fragment_week_listTasks);

        spinnerOrderBy = (Spinner) view.findViewById(R.id.fragment_week_spinner_orderBy);
        final ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.array_orderBy_week, android.R.layout.simple_spinner_item);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderBy.setAdapter(adap);

        initTaskList();

        spinnerOrderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case 0:
                        orderByDay();
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
                orderByDay();
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TaskModel tm = adapter.getItem(position);
                Fragment frag = new ViewTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id",tm.getId());
                frag.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container_body,frag);
            }
        });

    }

    private void initTaskList(){


        adapter = new WeekTaskListAdapter(getContext(), R.layout.week_list_item, taskList);
        listView.setAdapter(adapter);

        Query queryRef = tasksRef.orderByPriority();

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Carregando atividades");
        pd.setCancelable(false);
        pd.show();

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    TaskModel tm = ds.getValue(TaskModel.class);
                    taskList.add(tm);
                }
                pd.dismiss();
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


    private void orderByDay(){
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
