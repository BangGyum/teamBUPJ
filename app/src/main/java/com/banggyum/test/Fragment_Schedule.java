package com.banggyum.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class Fragment_Schedule extends Fragment {
    ListView customListView;
    ArrayList<com.banggyum.test.Schedule> listItem;
    private static CustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__schedule, container, false);
        FloatingActionButton floatingActionButton = v.findViewById(R.id.btn_fab);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PopupActivity.class));
            }
        });


//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View v = inflater.inflate(R.layout.schedule_list, container, false);

            listItem = new ArrayList<>();
            listItem.add(new com.banggyum.test.Schedule("00:00", "텍스트1", "위치1"));
            listItem.add(new com.banggyum.test.Schedule("00:03", "텍스트2", "위치2"));

            customListView = (ListView) v.findViewById(R.id.custom);
            customAdapter = new CustomAdapter(getContext(), listItem);
            customListView.setAdapter(customAdapter);
            customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedItem = (String) view.findViewById(R.id.sd_time).getTag().toString();
                }
            });
            return v;
        }
}

    class Schedule {
        private String time1;
        private String text1;
        private String position;

        public Schedule(String time1, String text1, String position){
            this.time1 = time1;
            this.text1 = text1;
            this.position = position;
        }

        public String getTime1(){
            return time1;
        }
        public String getText1(){
            return text1;
        }
        public String getPosition(){
            return position;
        }
    }

