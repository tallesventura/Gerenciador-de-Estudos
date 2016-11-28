package com.talles.android.daa_ti_tallesoliveira.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.talles.android.daa_ti_tallesoliveira.model.TaskModel;
import com.talles.android.daa_ti_tallesoliveira.R;

import java.util.List;

/**
 * Created by talles on 11/27/16.
 */

public class WeekTaskListAdapter extends ArrayAdapter<TaskModel> {

    public WeekTaskListAdapter(Context context, int resource, List<TaskModel> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TaskModel tm = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.week_list_item, parent, false);
            viewHolder.txtDay = (TextView) convertView.findViewById(R.id.week_day);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.week_type);
            viewHolder.txtSubject = (TextView) convertView.findViewById(R.id.week_subject);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.week_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtDay.setText(tm.getDay());
        viewHolder.txtType.setText(tm.getType());
        viewHolder.txtSubject.setText(tm.getSubject());
        viewHolder.txtDescription.setText(tm.getDescription());

        return convertView;
    }

    private static class ViewHolder {
        TextView txtDay;
        TextView txtType;
        TextView txtSubject;
        TextView txtDescription;
    }
}
