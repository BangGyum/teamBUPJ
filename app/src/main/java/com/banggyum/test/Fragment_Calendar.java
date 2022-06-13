package com.banggyum.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Fragment_Calendar extends Fragment {

    private MaterialCalendarView materialCalendar;
    private List<ScheduleDTO> selectScheList = new ArrayList<ScheduleDTO>();
    private MyDatabaseHelper db ;
    private TextView text;
    private TextView cal_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__calendar, container, false);

         ArrayList<Holidays> al = new ArrayList<>();
         ArrayList<Holidays> holidaysArrayList = new ArrayList<>();
         ArrayList<CalendarDay> calendarDayList = new ArrayList<>();


         for (int i=0; i < holidaysArrayList.toArray().length; i++) {

             String holidayDate = holidaysArrayList.get(i).getDate();

             DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.KOREA);
             CalendarDay date = CalendarDay.from(LocalDate.parse(holidayDate, dtf));
             calendarDayList.add(date);

        }

/*        //임의로 리스트 생성
        ArrayList<CalendarDay> calendarDayList = new ArrayList<>();
        calendarDayList.add(CalendarDay.from(2022, 06, 10));
        calendarDayList.add(CalendarDay.from(2022, 06, 13));
        calendarDayList.add(CalendarDay.from(2022, 06, 15));
        calendarDayList.add(CalendarDay.from(2022, 06, 16));
        calendarDayList.add(CalendarDay.from(2022, 06, 19));*/

        materialCalendar = v.findViewById(R.id.materialCalendar);
        text = v.findViewById(R.id.text);
        cal_text = v.findViewById(R.id.cal_text);

        materialCalendar.addDecorators(
                new Calendar_Event(calendarDayList, getActivity(), cal_text), //일정 등록이 되어있는 날짜 이벤트
                new Calendar_Saturday_Color(),  //토요일 색상
                new Calendar_Sunday_Color(), // 일요일 색상
                new Calendar_Today()); // 오늘 색상

        materialCalendar.setOnDateChangedListener(new OnDateSelectedListener() { //날짜 선택 텍스트
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView calendar, @NonNull CalendarDay date, boolean selected) {

                String wdate = date.getDate().toString();

                //일정 select
                selectScheList = db.selectDateSchedules(wdate);
                for (int i=0; i<selectScheList.size(); i++) {
                    ScheduleDTO sdSelect;
                    sdSelect = selectScheList.get(i);

                    cal_text.setText("제목 : " + sdSelect.getSchedule_context());
                }

                int year = date.getYear();// 연도
                int month = date.getMonth(); // 월
                int day = date.getDay(); // 일

                text.setText(year + "년 " + month + "월 " + day + "일"); // 저장된 날짜 텍스트로 불러오기
            }
        });

        return v;
    }
}