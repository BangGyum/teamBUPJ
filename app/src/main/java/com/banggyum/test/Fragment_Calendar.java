package com.banggyum.test;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;

public class Fragment_Calendar extends Fragment {

    private MaterialCalendarView materialCalendar;
    private TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__calendar, container, false);

        materialCalendar = v.findViewById(R.id.materialCalendar);
        text = v.findViewById(R.id.text);


        materialCalendar.addDecorators(
                new Calendar_Sunday_Color(), // 일요일 색상
                new Calendar_Saturday_Color());// 토요일 색상



        materialCalendar.setOnDateChangedListener(new OnDateSelectedListener() { //날짜 선택 텍스트
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView calendar, @NonNull CalendarDay date, boolean selected) {

                int year = date.getYear();// 연도
                int month = date.getMonth(); // 월
                int day = date.getDay(); // 일


                text.setText(year + "년 " + month + "월 " + day + "일"); // 저장된 날짜 텍스트로 불러오기

            }
        });

        return v;
    }
}