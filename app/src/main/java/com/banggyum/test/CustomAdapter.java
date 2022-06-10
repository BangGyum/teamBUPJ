package com.banggyum.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

    private Context context;
    private List list;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    class ViewHolder {
        public TextView sd_time, sd_text, sd_posit;
    }

    public CustomAdapter(@NonNull Context context, ArrayList list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    public View getView(int position, View v, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (v == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            v = layoutInflater.inflate(R.layout.schedule_list, parent, false);
        }
        viewHolder = new ViewHolder();
        viewHolder.sd_time = (TextView) v.findViewById(R.id.sd_time);
        viewHolder.sd_text = (TextView) v.findViewById(R.id.sd_text);
        viewHolder.sd_posit = (TextView) v.findViewById(R.id.sd_posit);

//        final Schedule schedule = (Schedule) list.get(position);
//        viewHolder.sd_time.setText(schedule.getTime1());
//        viewHolder.sd_text.setText(schedule.getText1());
//        viewHolder.sd_posit.setText(schedule.getPosition());


        return v;
    }
}
