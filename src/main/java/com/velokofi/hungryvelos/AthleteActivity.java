package com.velokofi.hungryvelos;

import java.io.Serializable;

public class AthleteActivity implements Serializable {

    /*
    "resource_state": 2,
        "athlete": {
            "id": 37177283,
            "resource_state": 1
        },
        "name": "Morning Ride",
        "distance": 254.4,
        "moving_time": 99,
        "elapsed_time": 184,
        "total_elevation_gain": 0.0,
        "type": "Ride",
        "workout_type": null,
        "id": 4564044221,
        "external_id": "shealthc0902a36-8a0a-4a46-a2db-0a478dc7e75a.tcx",
        "upload_id": 4876036333,
        "start_date": "2021-01-04T05:09:48Z",
        "start_date_local": "2021-01-04T10:39:48Z",
        "timezone": "(GMT+05:30) Asia/Kolkata",
        "utc_offset": 19800.0,
     */

    private int resource_state;

    private Athlete athlete;

    private String name;

    private long distance;

    private long moving_time;

    private long elapsed_time;

    private double total_elevation_gain;

    private String type;

    private long id;

    private String start_date;

    private String start_date_local;

    private String timezone;

    private double utc_offset;

    public int getResource_state() {
        return resource_state;
    }

    public void setResource_state(int resource_state) {
        this.resource_state = resource_state;
    }

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public long getMoving_time() {
        return moving_time;
    }

    public void setMoving_time(long moving_time) {
        this.moving_time = moving_time;
    }

    public long getElapsed_time() {
        return elapsed_time;
    }

    public void setElapsed_time(long elapsed_time) {
        this.elapsed_time = elapsed_time;
    }

    public double getTotal_elevation_gain() {
        return total_elevation_gain;
    }

    public void setTotal_elevation_gain(double total_elevation_gain) {
        this.total_elevation_gain = total_elevation_gain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_date_local() {
        return start_date_local;
    }

    public void setStart_date_local(String start_date_local) {
        this.start_date_local = start_date_local;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public double getUtc_offset() {
        return utc_offset;
    }

    public void setUtc_offset(double utc_offset) {
        this.utc_offset = utc_offset;
    }

    public static final class Athlete implements Serializable {
        private long id;
        private int resource_state;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getResource_state() {
            return resource_state;
        }

        public void setResource_state(int resource_state) {
            this.resource_state = resource_state;
        }
    }

}
