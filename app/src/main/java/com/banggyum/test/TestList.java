package com.banggyum.test;

import java.util.ArrayList;

public class TestList {
    static ArrayList<Holidays> holidaysArrayList = new ArrayList<>();
    public static ArrayList<Holidays> holidayArray() {
        addHolidaysItem("20220610" ,"공휴일1");
        addHolidaysItem("20220616" ,"공휴일2");
        addHolidaysItem("20220619" ,"공휴일3");

        return holidaysArrayList;
    }

    private static void addHolidaysItem(String date, String name ){
        Holidays item = new Holidays();
        item.setDate(date);
        item.setName(name);
        holidaysArrayList.add(item);
    }
}
