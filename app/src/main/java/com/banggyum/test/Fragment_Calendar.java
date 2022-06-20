package com.banggyum.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Fragment_Calendar extends Fragment {

    private MaterialCalendarView materialCalendar;
    private HashMap<CalendarDay, String> hol = new HashMap<>(); // 공휴일 날짜와 이름 담는 곳
    private List<ScheduleDTO> selectScheList = new ArrayList<ScheduleDTO>(); // 리사이클러뷰 일정 리스트
    private List<ScheduleDTO> selectScheList1 = new ArrayList<ScheduleDTO>(); //캘린더 이벤트 일정 리스트
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

        materialCalendar = v.findViewById(R.id.materialCalendar);
        text = v.findViewById(R.id.text);
        cal_tv = v.findViewById(R.id.cal_tv);
        sch_tv = v.findViewById(R.id.sch_tv);
        cal_Layout = v.findViewById(R.id.cal_Layout);

        //캘린더 리사이클러뷰
        recyclerView = v.findViewById(R.id.cal_rcv);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        //리사이클러 아이템 생성자
        listItem = new ArrayList<>();

        ArrayList<CalendarDay> eventDay = new ArrayList<>();
        //캘린더 일정 리스트
        ArrayList<CalendarDay> calendarDayList = new ArrayList<>();

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
            //String을 CalendarDay형식으로 포맷
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.KOREA);
            CalendarDay eventDate = CalendarDay.from(LocalDate.parse(event, dtf));
            calendarDayList.add(eventDate);
        }
        // 법정공휴일 리스트
        ArrayList<CalendarDay> HolidayList = new ArrayList<>();
        String json = "";
        JSONArray holidayArray = new JSONArray(); // 공휴일 json 파일 안의 날짜와 이름을 담는 곳

        try {
            InputStream is = getActivity().getAssets().open("holiday.json");//JSON파일 읽어오기

            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            //JSON파일 각 경로 데이터 저장
            JSONObject jsonObject = new JSONObject(json);
            String response = jsonObject.getString("response");
            JSONObject jsonObject1 = new JSONObject(response);
            String body = jsonObject1.getString("body");
            JSONObject jsonObject2 = new JSONObject(body);
            String items = jsonObject2.getString("items");
            JSONObject jsonObject3 = new JSONObject(items);

            holidayArray = jsonObject3.getJSONArray("item");

            //locdate(날짜 데이터 변환하여 저장)
            for (int i = 0; i < holidayArray.length(); i++) {
                JSONObject holidayObject = holidayArray.getJSONObject(i);
                String day = holidayObject.getString("locdate");

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.KOREA);
                CalendarDay date = CalendarDay.from(LocalDate.parse(day, dtf));
                HolidayList.add(date);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        materialCalendar.addDecorators( //캘린더에 데코레이터 적용
                new Calendar_Event(calendarDayList, getActivity(), cal_Layout), //일정 등록이 되어있는 날짜 이벤트
                new Calendar_Saturday_Color(),  //토요일 색상
                new Calendar_Sunday_Color(), // 일요일 색상
                new HolidayColor(HolidayList),// 공휴일 색상
                new Calendar_Today()); // 오늘 색상

        JSONArray finalHolidayArray = holidayArray; // 공휴일 날짜와 이름이 담긴 리스트
        materialCalendar.setOnDateChangedListener(new OnDateSelectedListener() { //날짜 선택 텍스트
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView calendar, @NonNull CalendarDay date, boolean selected) {
                int year = date.getYear();// 연도
                int month = date.getMonth(); // 월
                int day = date.getDay(); // 일

                selectScheList = db.selectDateSchedules(date.getDate().toString());

                // 휴일명
                String name = "";
                for (int i = 0; i < finalHolidayArray.length(); i++) {
                    JSONObject holidayObject = null;
                    try {
                        holidayObject = finalHolidayArray.getJSONObject(i);
                        String d1 = holidayObject.getString("locdate");
                        String n1 = holidayObject.getString("dateName");

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.KOREA);
                        CalendarDay d2 = CalendarDay.from(LocalDate.parse(d1, dtf));
                        hol.put(d2, n1);

                        if (hol.containsKey(date)) {
                            name = hol.get(date);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                text.setText(year + "년 " + month + "월 " + day + "일" + "\n"); // 저장된 날짜 텍스트로 불러오기
                text.append(name);

                listRefresh(); // 레이아웃 초기화(다른날짜 누르면 초기화)
                listItem.clear(); //아이템 초기화
                addRecylerItem(); // 아이템 추가
            }
        });
        return v;
    }

    public void addRecylerItem() {
        //DB에 일정들 순서대로 추가
        for (int i=0; i<selectScheList.size(); i++){
            ScheduleDTO sdSelect;
            sdSelect = selectScheList.get(i);

            ScheduleDTO SD = new ScheduleDTO(sdSelect.getSchedule_id(), sdSelect.getSchedule_context(),
                    sdSelect.getSchedule_date(), sdSelect.getSchedule_time(),sdSelect.getSchedule_location(),
                    sdSelect.getSchedule_state(), sdSelect.getSchedule_registerDate(), sdSelect.getSchedule_registerDate1());
            listItem.add(SD);
            calendar_Item.notifyDataSetChanged();
        }
    }
    private void listRefresh() {
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(calendar_Item);
    }
}