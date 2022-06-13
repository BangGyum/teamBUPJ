package com.banggyum.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<ItemObject> mListItemms;
    private IClickListener iclickListener;

    public ItemAdapter(List<ItemObject> mListItemms, IClickListener iclickListener){
        this.mListItemms = mListItemms;
        this.iclickListener = iclickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //추가할 데이터 값을 가져옴
        ItemObject itemObject = mListItemms.get(position);
        if(itemObject == null){
            return;
        }

        //텍스트뷰에 데이터 표시
        holder.tvitem.setText(itemObject.getName());
        //텍스트뷰 선택시 선택된 값 가져옴
        holder.tvitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iclickListener.clickItem(itemObject);
            }
        });
    }

    //추가해야 될 아이템 개수 확인
    @Override
    public int getItemCount() {
        if(mListItemms != null){
            return mListItemms.size();
        }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvitem;

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);

            //텍스트뷰 사용을 위해 연결
            tvitem = itemView.findViewById(R.id.tv_item);
        }
    }
}
