package com.banggyum.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ScheduleBottomSheet extends BottomSheetDialogFragment {
    private EditText edsc, eddate, edloc, edtime;
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

        eddate = view.findViewById(R.id.ed_date);
        edsc = view.findViewById(R.id.ed_sc);
        edloc = view.findViewById(R.id.ed_loc);
        edtime = view.findViewById(R.id.ed_time);
        rgb1 = view.findViewById(R.id.rgb1);
        btn_cls = view.findViewById(R.id.cls_btn);
        btn_update = view.findViewById(R.id.update_btn);

        edsc.setText(sd.getSchedule_context());
        eddate.setText(sd.getSchedule_date());
        edtime.setText(sd.getSchedule_time());
        edloc.setText(sd.getSchedule_location());

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
