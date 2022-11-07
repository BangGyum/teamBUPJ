package com.banggyum.test;

public class AlarmDTO {
    private int schedule_id_fk;
    private String alarm_date;
    private String alarm_time;
    private String schedule_date;
    private String schedule_time;

    public AlarmDTO(){
    }

    public AlarmDTO(int schedule_id_fk, String alarm_date, String alarm_time, String schedule_date, String schedule_time) {
        this.schedule_id_fk = schedule_id_fk;
        this.alarm_date = alarm_date;
        this.alarm_time = alarm_time;
        this.schedule_date = schedule_date;
        this.schedule_time = schedule_time;
    }

    public int getSchedule_id_fk() {
        return schedule_id_fk;
    }

    public void setSchedule_id_fk(int schedule_id_fk) {
        this.schedule_id_fk = schedule_id_fk;
    }

    public String getAlarm_date() {
        return alarm_date;
    }

    public void setAlarm_date(String alarm_date) {
        this.alarm_date = alarm_date;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }
}