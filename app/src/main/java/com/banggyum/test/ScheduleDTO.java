package com.banggyum.test;

public class ScheduleDTO {
    private int schedule_id;
    //private String user_id;
    private String schedule_context;
    private String schedule_date;
    private String schedule_time;
    private String schedule_location;
    private short schedule_state;
    private String schedule_registerDate1;
    private String schedule_registerDate;

    public ScheduleDTO() {
    }

    public String getSchedule_registerDate() {
        return schedule_registerDate;
    }

    public ScheduleDTO(String schedule_context) {
        this.schedule_context = schedule_context;
    }

    public ScheduleDTO(int schedule_id, String schedule_context, String schedule_date, String schedule_time,String schedule_location, short schedule_state, String schedule_registerDate1, String schedule_registerDate) {
        this.schedule_id = schedule_id;
        this.schedule_context = schedule_context;
        this.schedule_date = schedule_date;
        this.schedule_time = schedule_time;
        this.schedule_location = schedule_location;
        this.schedule_state = schedule_state;
        this.schedule_registerDate1 = schedule_registerDate1;
        this.schedule_registerDate = schedule_registerDate;
    }

    public void setSchedule_registerDate(String schedule_registerDate) {
        this.schedule_registerDate = schedule_registerDate;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

//    public String getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(String user_id) {
//        this.user_id = user_id;
//    }

    public String getSchedule_context() {
        return schedule_context;
    }

    public void setSchedule_context(String schedule_context) {
        this.schedule_context = schedule_context;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }
    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public String getSchedule_location() {
        return schedule_location;
    }

    public void setSchedule_location(String schedule_location) {
        this.schedule_location = schedule_location;
    }

    public short getSchedule_state() {
        return schedule_state;
    }

    public void setSchedule_state(short schedule_state) {
        this.schedule_state = schedule_state;
    }

    public String getSchedule_registerDate1() {
        return schedule_registerDate1;
    }

    public void setSchedule_registerDate1(String schedule_registerDate) {
        this.schedule_registerDate1 = schedule_registerDate;
    }
}