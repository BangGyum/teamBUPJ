package com.banggyum.test;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleBottomSheet extends BottomSheetDialogFragment {
    private EditText edsc, edloc;
    private RadioButton rgb1;
    private Button btn_cls, btn_update, btn_date, btn_time, btn_map;
    private TextView timeudate, dateupdate;
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
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.schedule_bottomsheet, container, false);


        edsc = view.findViewById(R.id.ed_sc);
        edloc = view.findViewById(R.id.ed_loc);

        rgb1 = view.findViewById(R.id.rgb1);
        btn_cls = view.findViewById(R.id.cls_btn);
        btn_update = view.findViewById(R.id.update_btn);


        timeudate = view.findViewById(R.id.updatetime_view);
        dateupdate = view.findViewById(R.id.updatedate_view);
        btn_date = view.findViewById(R.id.dateupdatebtn);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), myDatePicker,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btn_time = view.findViewById(R.id.timeupdatebtn);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);                //한국시간 : +9
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                        }
                        timeudate.setText(state + " " + selectedHour + "시" +
                                selectedMinute + "분");
                    }
                },hour,minute,false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        btn_map = view.findViewById(R.id.mapupdateBtn);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDB = new Intent(view.getContext(), InsertMapDB.class);
                startActivityForResult(intentDB, 1);

            }
        });


        edsc.setText(sd.getSchedule_context());
        dateupdate.setText(sd.getSchedule_date());
        timeudate.setText(sd.getSchedule_time());
        edloc.setText(sd.getSchedule_location());

        rgb1.setOnClickListener(rgbListener);
        btn_cls.setOnClickListener(btnListener);
        btn_update.setOnClickListener(btnListener);
        return view;
    }
    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2021/07/26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        dateupdate.setText(sdf.format(myCalendar.getTime()));
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
                    Toast.makeText(getContext(), "닫기버튼", Toast.LENGTH_SHORT).show();
                    dismiss();
                    break;
                case R.id.update_btn:
                    Toast.makeText(getContext(), sd.getSchedule_id()+"", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    String roadAddress, searchName;
    Double lat, lng;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                searchName = data.getStringExtra("searchname");
                lat = data.getDoubleExtra("lat", 0);
                lng = data.getDoubleExtra("lng", 0);

                edloc.setText(searchName);
            }
        }
    }
}
