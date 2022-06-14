package com.banggyum.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ScheduleBottomSheet extends BottomSheetDialogFragment {
    private TextView txt_context, txt_date, txt_loc;
    private RadioButton rgb1;
    private Button btn_cls, btn_update;
    private ScheduleDTO sd;
    private ScheduleItemAdapter scheduleItemAdapter;

    public ScheduleBottomSheet(ScheduleDTO sd) {
        this.sd = sd;
    }

    public ScheduleBottomSheet() {
    }

    public static ScheduleBottomSheet newInstance(ScheduleDTO scheduleDTO){
        ScheduleBottomSheet scheduleBottomSheet = new ScheduleBottomSheet();
        return scheduleBottomSheet;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.schedule_bottomsheet, container, false);

        txt_context = view.findViewById(R.id.txt_sch);
        txt_date = view.findViewById(R.id.txt_date);
        txt_loc = view.findViewById(R.id.txt_loc);
        rgb1 = view.findViewById(R.id.rgb1);
        btn_cls = view.findViewById(R.id.cls_btn);
        btn_update = view.findViewById(R.id.update_btn);

        txt_context.setText("일정: " + sd.getSchedule_context());
        txt_date.setText("일정: " + sd.getSchedule_date());
        txt_loc.setText("일정: " + sd.getSchedule_location());

        rgb1.setOnClickListener(rgbListener);
        btn_cls.setOnClickListener(btnListener);
        btn_update.setOnClickListener(btnListener);
        return view;
    }

    View.OnClickListener rgbListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    View.OnClickListener btnListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.cls_btn:
                    dismiss();
                    break;
                case R.id.update_btn:

                    break;
            }
        }
    };
}
