package com.banggyum.test;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
//리사이클러뷰(캘린더) 어댑터
public class Calendar_Item extends RecyclerView.Adapter<Calendar_Item.ItemViewHolder>{

    private List<ScheduleDTO> mListItems;
    private MyDatabaseHelper db;

    public Calendar_Item(List<ScheduleDTO> mListItems, Fragment_Calendar fragment_calendar) {
        this.mListItems = mListItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        //DB를 사용하기위한 생성자
        db = new MyDatabaseHelper(view.getContext());
        return itemViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position ) {
        ScheduleDTO SD = mListItems.get(position);
        if (SD == null){
            return;
        }
        holder.itemView.setTag(position);
        holder.cal_tv.setText("제목 : " + SD.getSchedule_context() + "\n");
        holder.cal_tv.append("날짜 : " + SD.getSchedule_date() + "\n");
        holder.cal_tv.append("시간 : " + SD.getSchedule_time() + "\n");
        //holder.cal_tv.append("장소 : " + SD.getSchedule_location() + "\n");
        holder.cal_tv.append("\n");
    }

    @Override
    public int getItemCount() {
        if(mListItems != null){
            return mListItems.size();
        }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView cal_tv;
        private ConstraintLayout cal_layout;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cal_tv = itemView.findViewById(R.id.cal_tv);
            cal_layout = itemView.findViewById(R.id.cal_Layout);
        }
    }
}