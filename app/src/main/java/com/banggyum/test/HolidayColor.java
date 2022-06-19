package com.banggyum.test;

import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class HolidayColor implements DayViewDecorator {

    private HashSet<CalendarDay> dates;

    public HolidayColor(Collection<CalendarDay> dates){
        this.dates = new HashSet<>(dates);
    }

    public HolidayColor() {

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(android.graphics.Color.RED));
    }
}
