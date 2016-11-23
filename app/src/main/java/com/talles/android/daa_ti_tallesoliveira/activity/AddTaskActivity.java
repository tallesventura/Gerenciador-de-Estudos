package com.talles.android.daa_ti_tallesoliveira.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.talles.android.daa_ti_tallesoliveira.R;

public class AddTaskActivity extends AppCompatActivity {

    private Spinner spinnerDaysWeek;
    private Spinner spinnerTaskTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_task);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        this.spinnerDaysWeek = (Spinner) findViewById(R.id.days_week);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_days_of_the_week, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerDaysWeek.setAdapter(adapter);

        this.spinnerTaskTypes = (Spinner) findViewById(R.id.add_task_type);
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_task_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerTaskTypes.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu. menu_add_task, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
