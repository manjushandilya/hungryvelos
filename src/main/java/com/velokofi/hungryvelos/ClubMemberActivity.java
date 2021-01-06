package com.velokofi.hungryvelos;

/*
    {
        "resource_state": 2,
        "athlete": {
            "resource_state": 2,
            "firstname": "Ravi",
            "lastname": "S."
        },
        "name": "Morning Ride",
        "distance": 17779.7,
        "moving_time": 4312,
        "elapsed_time": 8302,
        "total_elevation_gain": 101.3,
        "type": "Ride",
        "workout_type": null
    }
 */

public class ClubMemberActivity {

    private int resource_state;
    private ClubActivityAthlete clubActivityAthlete;
    private String name;
    private float distance;
    private long moving_time;
    private long elapsed_time;
    private float total_elevation_gain;
    private String type;
    private String workout_type;

    public int getResource_state() {
        return resource_state;
    }

    public void setResource_state(int resource_state) {
        this.resource_state = resource_state;
    }

    public ClubActivityAthlete getAthlete() {
        return clubActivityAthlete;
    }

    public void setAthlete(ClubActivityAthlete clubActivityAthlete) {
        this.clubActivityAthlete = clubActivityAthlete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
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

    public float getTotal_elevation_gain() {
        return total_elevation_gain;
    }

    public void setTotal_elevation_gain(float total_elevation_gain) {
        this.total_elevation_gain = total_elevation_gain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkout_type() {
        return workout_type;
    }

    public void setWorkout_type(String workout_type) {
        this.workout_type = workout_type;
    }

    public String getAthleteName() {
        return clubActivityAthlete.getFirstname() + " " + clubActivityAthlete.getLastname();
    }

    @Override
    public String toString() {
        return "Activity{" +
                "resource_state=" + resource_state +
                ", athlete=" + clubActivityAthlete +
                ", name='" + name + '\'' +
                ", distance=" + distance +
                ", moving_time=" + moving_time +
                ", elapsed_time=" + elapsed_time +
                ", total_elevation_gain=" + total_elevation_gain +
                ", type='" + type + '\'' +
                ", workout_type='" + workout_type + '\'' +
                '}';
    }

}
