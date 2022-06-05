package com.banggyum.test;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.DayOfWeek;

import java.util.Calendar;

public class Calendar_Saturday_Color implements DayViewDecorator {
    private Calendar calendar = Calendar.getInstance();
    private CalendarDay date;

    public Calendar_Saturday_Color() {


    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {

        int weekday = day.getDate().with(DayOfWeek.SATURDAY).getDayOfMonth();
        return weekday == day.getDay();
    }//여기서 true가 리턴되면 decorate 함수가 실행된다.

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }

}

