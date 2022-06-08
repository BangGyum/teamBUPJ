package com.banggyum.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleItemAdapter extends RecyclerView.Adapter<ScheduleItemAdapter.ItemViewHolder>{

    private List<ScheduleDTO> mListItems;

    public ScheduleItemAdapter(List<ScheduleDTO> mListItems) {
        this.mListItems = mListItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleview_item, parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ScheduleDTO SD = mListItems.get(position);
        if (SD == null){
            return;
        }

        holder.itemView.setTag(position);
        holder.tv.setText(SD.getSchedule_location());
        holder.rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(holder.getAbsoluteAdapterPosition());
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

    public void remove(int position){
        try{
            mListItems.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private RadioButton rb;
        private TextView tv;
        private ConstraintLayout cl;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rb = itemView.findViewById(R.id.itemrb);
            tv = itemView.findViewById(R.id.itemtv);
            cl = itemView.findViewById(R.id.Layout11);
        }
    }
}
