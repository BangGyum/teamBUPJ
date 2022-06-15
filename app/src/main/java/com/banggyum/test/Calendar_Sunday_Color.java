package com.banggyum.test;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.DayOfWeek;

import java.util.Calendar;
// 캘린더 일요일 색상
public class Calendar_Sunday_Color implements DayViewDecorator {
    private Calendar calendar = Calendar.getInstance();
    private CalendarDay date;

    public Calendar_Sunday_Color() {


    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {

        int weekday = day.getDate().with(DayOfWeek.SUNDAY).getDayOfMonth();
        return weekday == day.getDay();
    }//return값이 true면 decorate 함수가 실행된다.
    @Override
    public void decorate(DayViewFacade view) {

        view.addSpan(new ForegroundColorSpan(Color.RED));
    }

}
