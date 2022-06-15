package com.banggyum.test;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ScheduleItemAdapter extends RecyclerView.Adapter<ScheduleItemAdapter.ItemViewHolder> {

    private List<ScheduleDTO> mListItems;
    private ItemClickListener itemClickListener;
    private MyDatabaseHelper db;

    public ScheduleItemAdapter(List<ScheduleDTO> mListItems, ItemClickListener itemClickListener) {
        this.mListItems = mListItems;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleview_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        //DB를 사용하기위한 생성자
        db = new MyDatabaseHelper(view.getContext());
        return itemViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ScheduleDTO SD = mListItems.get(position);
        if (SD == null){
            return;
        }

        holder.itemView.setTag(position);
        holder.tv.setText("일정 : " + SD.getSchedule_context() + "\n");
        holder.rb.setChecked(false);
        holder.rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(holder.getAbsoluteAdapterPosition(), SD.getSchedule_id(), SD.getSchedule_state());
                @SuppressLint("ShowToast") Snackbar snackbar = Snackbar.make(holder.cl, "완료했습니다.", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.setAction("실행취소", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        undoItem(SD, position);
                    }
                });
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
            }
        });

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(mListItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListItems != null){
            return mListItems.size();
        }
        return 0;
    }

    public void remove(int position, int sdId, int state){
        try{
            db.updateSchedule(sdId, state);
            mListItems.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void undoItem(ScheduleDTO SD, int position){
        try{
            int state = db.selectState(SD.getSchedule_id());
            db.updateSchedule(SD.getSchedule_id(), state);
            mListItems.add(position, SD);
            notifyDataSetChanged();
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private RadioButton rb;
        private TextView tv;
        private LinearLayout cl;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rb = itemView.findViewById(R.id.itemrb);
            tv = itemView.findViewById(R.id.itemtv);
            cl = itemView.findViewById(R.id.Layout11);
        }
    }

    public interface ItemClickListener{
        public void onItemClick(ScheduleDTO scheduleDTO);
    }
}
