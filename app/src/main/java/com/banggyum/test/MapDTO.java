package com.banggyum.test;

public class MapDTO {
    private int schedule_id_fk;
    private String map_name;
    private double map_latitude;
    private double map_longitude;

    public MapDTO() {
    }

    public MapDTO(int schedule_id_fk, String map_name, double map_latitude, double map_longitude) {
        this.schedule_id_fk = schedule_id_fk;
        this.map_name = map_name;
        this.map_latitude = map_latitude;
        this.map_longitude = map_longitude;
    }

    public int getSchedule_id_fk() {
        return schedule_id_fk;
    }

    public void setSchedule_id_fk(int schedule_id_fk) {
        this.schedule_id_fk = schedule_id_fk;
    }

    public String getMap_name() {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public double getMap_latitude() {
        return map_latitude;
    }

    public void setMap_latitude(double map_latitude) {
        this.map_latitude = map_latitude;
    }

    public double getMap_longitude() {
        return map_longitude;
    }

    public void setMap_longitude(double map_longitude) {
        this.map_longitude = map_longitude;
    }
}

