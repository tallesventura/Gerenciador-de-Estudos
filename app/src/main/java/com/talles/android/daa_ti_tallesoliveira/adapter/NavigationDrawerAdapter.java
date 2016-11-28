package com.talles.android.daa_ti_tallesoliveira.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.talles.android.daa_ti_tallesoliveira.R;
import com.talles.android.daa_ti_tallesoliveira.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by talles on 9/25/16.
 */

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Integer> icons;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data, ArrayList<Integer> icons) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.icons = icons;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.icon.setImageResource(icons.get(position).intValue());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            icon = (ImageView) itemView.findViewById(R.id.nav_drawer_row_icon);
        }
    }
}