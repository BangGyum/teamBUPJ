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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleBottomSheet extends BottomSheetDialogFragment{
    private EditText edsc, edloc, timeudate, dateupdate;
    private Button btn_cls, btn_update;
    private ImageButton btn_date, btn_time, btn_map;
    private ScheduleDTO sd;
    private MyDatabaseHelper db;
    private String date;

    public ScheduleBottomSheet() {
    }

    public ScheduleBottomSheet(ScheduleDTO sd) {
        this.sd = sd;
    }

    public static ScheduleBottomSheet newInstance(ScheduleDTO scheduleDTO) {
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
            String y = Integer.toString(year);
            String m = Integer.toString(month+1); // 5월이면 05가 아니라 5임
            //month가 인덱스라 그런지 1개월 어디에 버리고 나오기 때문에 +1
            String d = Integer.toString(dayOfMonth);
            // 10보다 작으면 앞에 0 붙여주기
            if (month<9) {
                m = "0" + m;
            }

            if (dayOfMonth<10){
                d = "0" + d;
            }
            date = y + "-" + m + "-" + d ;
            updateLabel();
        }
    };

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_bottomsheet, container, false);
        //xml로 정의된 view (또는 menu 등)를 실제 객체화 시키는 용도
        edsc = view.findViewById(R.id.ed_sc);
        edloc = view.findViewById(R.id.ed_loc);

        btn_cls = view.findViewById(R.id.cls_btn);
        btn_update = view.findViewById(R.id.update_btn);

        timeudate = view.findViewById(R.id.updatetime_view);
        dateupdate = view.findViewById(R.id.updatedate_view);
        btn_date = view.findViewById(R.id.dateupdatebtn);
        db = new MyDatabaseHelper(getContext());

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
                mTimePicker = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String state = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        String h = Integer.toString(selectedHour);
                        String m = Integer.toString(selectedMinute);
                        if (selectedHour < 10) {
                            h = "0" + h;
                        }
                        if (selectedMinute < 10) {
                            m = "0" + m;
                        }
                        timeudate.setText(h +
                                m);
                    }
                }, hour, minute, false);
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
        date=sd.getSchedule_date();
        timeudate.setText(sd.getSchedule_time());
        //edloc.setText(sd.getSchedule_location());

        btn_cls.setOnClickListener(btnListener);
        btn_update.setOnClickListener(btnListener);
        return view;
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";    // 출력형식   2021/07/26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        dateupdate.setText(sdf.format(myCalendar.getTime()));
    }

    private String searchName;
    private Double lat, lng;

    View.OnClickListener btnListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cls_btn:
                    dismiss();
                    break;
                case R.id.update_btn:
                    db.updateSchedule(sd.getSchedule_id()
                            , edsc.getText().toString()
                            , date
                            , timeudate.getText().toString()
                            , edloc.getText().toString());

                    if(lat != null) {
                        db.updateMap(sd.getSchedule_id()
                                , edloc.getText().toString()
                                , lat
                                , lng);
                    }
                    dismiss();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Fragment_Schedule()).commit();
                    break;
            }
        }
    };

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
