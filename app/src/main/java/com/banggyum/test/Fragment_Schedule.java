package com.banggyum.test;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Schedule extends Fragment {
    private ArrayList<ScheduleDTO> listItem;
    private RecyclerView recyclerView;
    private ScheduleItemAdapter scheduleItemAdapter;
    private LinearLayoutManager linearLayoutManager;
    List<ScheduleDTO> selectScheList = new ArrayList<ScheduleDTO>();
    MyDatabaseHelper db ;
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment__schedule, null);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_fab);
        recyclerView = view.findViewById(R.id.rcv_sc);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        listItem = new ArrayList<>();

        db = new MyDatabaseHelper(view.getContext());

        scheduleItemAdapter = new ScheduleItemAdapter(listItem);
        recyclerView.setAdapter(scheduleItemAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDB = new Intent(view.getContext(), PopupActivity.class);
                startActivityForResult(intentDB, 1);
            }
        });
        addRecylerItem();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ScheduleDTO SD = new ScheduleDTO(1, "context", "date", "location", (short) 123, "regdate", "redate");
                listItem.add(SD);
                scheduleItemAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addRecylerItem(){
        selectScheList=db.selectSchedules();
        for (int i=0; i<selectScheList.size(); i++){
            ScheduleDTO sd = new ScheduleDTO();
            sd= selectScheList.get(i);

            Log.v("aaaa",sd.getSchedule_context());
        }
    }
}
//
//    class Schedule {
//        private String time1;
//        private String text1;
//        private String position;
//
//        public Schedule(String time1, String text1, String position){
//            this.time1 = time1;
//            this.text1 = text1;
//            this.position = position;
//        }
//
//        public String getTime1(){
//            return time1;
//        }
//        public String getText1(){
//            return text1;
//        }
//        public String getPosition(){
//            return position;
//        }
//    }
