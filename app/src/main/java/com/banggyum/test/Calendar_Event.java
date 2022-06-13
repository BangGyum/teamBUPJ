package com.banggyum.test;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

public class Calendar_Event implements DayViewDecorator {

    private final Drawable drawable;

    private HashSet<CalendarDay> dates;
    private TextView textView;

    public Calendar_Event(Collection<CalendarDay> dates, Activity context, TextView textView) {
        // 이미지
        drawable = context.getResources().getDrawable(R.drawable.calendar_background);

        this.dates = new HashSet<>(dates);
        this.textView = textView;

    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }
    //return값이 true면 decorate 함수가 실행된다.
    @Override
    public void decorate(DayViewFacade view) {

        view.setSelectionDrawable(drawable);
        view.addSpan(new ForegroundColorSpan(Color.BLACK)); //선택 날짜 안의 색상
    }

    public void setText(String text){
        textView.setText(text);
    }
}
