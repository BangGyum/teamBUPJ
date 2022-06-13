package com.banggyum.test;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
    private List<ScheduleDTO> selectScheList = new ArrayList<ScheduleDTO>();
    private MyDatabaseHelper db ;

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

        //DB를 사용하기위한 생성자
        db = new MyDatabaseHelper(view.getContext());

        //리사이클러에 내용들을 추가해주기 위해 어댑터에 아이템들을 넘겨줌
        scheduleItemAdapter = new ScheduleItemAdapter(listItem);
        //리사이클에 어댑터를 통해 아이템 추가 및 수정, 삭제
        recyclerView.setAdapter(scheduleItemAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //플롱팅버튼 클릭시 일정 추가위한 팝업 띄어줌
                Intent intentDB = new Intent(view.getContext(), PopupActivity.class);
                startActivityForResult(intentDB, 1);
            }
        });

        //DB에 있는 정보 recyleview에 추가
        addRecylerItem();
        return view;
    }

    //일정 추가 팝업 창에서 확인 버튼 클릭시 실행
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //리사이클뷰 초기화
                listItem.clear();
                //다시 추가
                addRecylerItem();
            }
        }
    }

    //리사이클뷰에 DB에 있는 일정들 추가
    @SuppressLint("NotifyDataSetChanged")
    public void addRecylerItem(){
        //일정 select
        selectScheList = db.selectSchedules();
        //DB에 일정들 순서대로 추가
        for (int i=0; i<selectScheList.size(); i++){
            ScheduleDTO sdSelect;
            sdSelect = selectScheList.get(i);

            ScheduleDTO SD = new ScheduleDTO(sdSelect.getSchedule_id(), sdSelect.getSchedule_context(), sdSelect.getSchedule_date(), sdSelect.getSchedule_time(),sdSelect.getSchedule_location(), sdSelect.getSchedule_state(), sdSelect.getSchedule_registerDate(), sdSelect.getSchedule_registerDate1());
            listItem.add(SD);
            scheduleItemAdapter.notifyDataSetChanged();
        }
    }

}
