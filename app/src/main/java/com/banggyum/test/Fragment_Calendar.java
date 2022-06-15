package com.banggyum.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private List<ScheduleDTO> selectScheList1 = new ArrayList<ScheduleDTO>();
    private ArrayList<ScheduleDTO> listItem;
    private MyDatabaseHelper db;
    private TextView text;
    private TextView cal_tv, sch_tv;
    private ConstraintLayout cal_Layout;
    private CalendarDay day;
    //
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Calendar_Item calendar_Item;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__calendar, container, false);

        //캘린더 리사이클러뷰
        recyclerView = v.findViewById(R.id.cal_rcv);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        ArrayList<CalendarDay> eventDay = new ArrayList<>();
        //캘린더 일정 리스트
        ArrayList<CalendarDay> calendarDayList = new ArrayList<>();

        listItem = new ArrayList<>();
        //DB 생성자
        db = new MyDatabaseHelper(v.getContext());
        //리사이클러에 내용들을 추가해주기 위해 어댑터에 아이템들을 넘겨줌
        calendar_Item = new Calendar_Item(listItem, this);
        //리사이클에 어댑터를 통해 아이템 추가 및 수정, 삭제
        recyclerView.setAdapter(calendar_Item);

        selectScheList1 = db.selectSchedules();
        //캘린더 등록된 일정 이벤트 리스트에 추가
        for (int i=0; i<selectScheList1.size(); i++) {
            ScheduleDTO sdAllSelect;
            sdAllSelect = selectScheList1.get(i);
            String event = sdAllSelect.getSchedule_date();
            Log.v("date", event);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.KOREA);
            CalendarDay eventDate = CalendarDay.from(LocalDate.parse(event, dtf));
            calendarDayList.add(eventDate);
        }


        materialCalendar = v.findViewById(R.id.materialCalendar);
        text = v.findViewById(R.id.text);
        cal_tv = v.findViewById(R.id.cal_tv);
        sch_tv = v.findViewById(R.id.sch_tv);
        cal_Layout = v.findViewById(R.id.cal_Layout);


        materialCalendar.addDecorators(
                new Calendar_Event(calendarDayList, getActivity(), cal_Layout), //일정 등록이 되어있는 날짜 이벤트
                new Calendar_Saturday_Color(),  //토요일 색상
                new Calendar_Sunday_Color(), // 일요일 색상
                new Calendar_Today()); // 오늘 색상


        materialCalendar.setOnDateChangedListener(new OnDateSelectedListener() { //날짜 선택 텍스트
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView calendar, @NonNull CalendarDay date, boolean selected) {
                //일정 select
                selectScheList = db.selectDateSchedules(date.getDate().toString());
                int year = date.getYear();// 연도
                int month = date.getMonth(); // 월
                int day = date.getDay(); // 일
                listItem.clear();
                addRecylerItem();


                text.setText(year + "년 " + month + "월 " + day + "일"); // 저장된 날짜 텍스트로 불러오기
            }
        });


        return v;
    }
    public void addRecylerItem() {
        //일정 select

        //DB에 일정들 순서대로 추가
        for (int i=0; i<selectScheList.size(); i++){
            ScheduleDTO sdSelect1;
            sdSelect1 = selectScheList.get(i);

            ScheduleDTO SD = new ScheduleDTO(sdSelect1.getSchedule_id(), sdSelect1.getSchedule_context(), sdSelect1.getSchedule_date(), sdSelect1.getSchedule_time(),sdSelect1.getSchedule_location(), sdSelect1.getSchedule_state(), sdSelect1.getSchedule_registerDate(), sdSelect1.getSchedule_registerDate1());
            listItem.add(SD);
            calendar_Item.notifyDataSetChanged();
        }
    }
}