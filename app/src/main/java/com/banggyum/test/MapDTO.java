package com.banggyum.test;

public class MapDTO {
    private int schedule_id_fk;
    private String map_name;
    private Double map_latitude;
    private Double map_longitude;

    public int getSchedule_id_fk() {
        return schedule_id_fk;
    }

    public void setSchedule_id_fk(int schedule_id_fk) {
        this.schedule_id_fk = schedule_id_fk;
    }

    public String getMap_name(String string) {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public Double getMap_latitude(double aDouble) {
        return map_latitude;
    }

    public void setMap_latitude(Double map_latitude) {
        this.map_latitude = map_latitude;
    }

    public Double getMap_longitude(double aDouble) {
        return map_longitude;
    }

    public void setMap_longitude(Double map_longitude) {
        this.map_longitude = map_longitude;
    }
}

