package com.velokofi.hungryvelos.model;

import com.velokofi.hungryvelos.model.AthleteProfile;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LeaderBoard {

    private Double totalDistance;
    private Double totalElevation;
    private int totalRides;
    private long movingTime;
    private Double riderAverage;
    private int riderCount;

    private String movingTimeInHumanReadableFormat;

    private Map<String, Double> teamProgressMap;
    private Map<Long, Double> athleteProgressMap;

    private List<Entry<String, Double>> teamTotals;

    private List<Entry<String, Double>> bettappa;
    private List<Entry<String, Double>> bettamma;
    private List<Entry<String, Double>> mrAlemaari;
    private List<Entry<String, Double>> msAlemaari;

    private AthleteProfile athleteProfile;

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getTotalElevation() {
        return totalElevation;
    }

    public void setTotalElevation(Double totalElevation) {
        this.totalElevation = totalElevation;
    }

    public int getTotalRides() {
        return totalRides;
    }

    public void setTotalRides(int totalRides) {
        this.totalRides = totalRides;
    }

    public long getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(long movingTime) {
        this.movingTime = movingTime;
    }

    public Double getRiderAverage() {
        return riderAverage;
    }

    public void setRiderAverage(Double riderAverage) {
        this.riderAverage = riderAverage;
    }

    public int getRiderCount() {
        return riderCount;
    }

    public void setRiderCount(int riderCount) {
        this.riderCount = riderCount;
    }

    public String getMovingTimeInHumanReadableFormat() {
        return movingTimeInHumanReadableFormat;
    }

    public void setMovingTimeInHumanReadableFormat(String movingTimeInHumanReadableFormat) {
        this.movingTimeInHumanReadableFormat = movingTimeInHumanReadableFormat;
    }

    public Map<String, Double> getTeamProgressMap() {
        return teamProgressMap;
    }

    public void setTeamProgressMap(Map<String, Double> teamProgressMap) {
        this.teamProgressMap = teamProgressMap;
    }

    public Map<Long, Double> getAthleteProgressMap() {
        return athleteProgressMap;
    }

    public void setAthleteProgressMap(Map<Long, Double> athleteProgressMap) {
        this.athleteProgressMap = athleteProgressMap;
    }

    public List<Entry<String, Double>> getTeamTotals() {
        return teamTotals;
    }

    public void setTeamTotals(List<Entry<String, Double>> teamTotals) {
        this.teamTotals = teamTotals;
    }

    public List<Entry<String, Double>> getBettappa() {
        return bettappa;
    }

    public void setBettappa(List<Entry<String, Double>> bettappa) {
        this.bettappa = bettappa;
    }

    public List<Entry<String, Double>> getBettamma() {
        return bettamma;
    }

    public void setBettamma(List<Entry<String, Double>> bettamma) {
        this.bettamma = bettamma;
    }

    public List<Entry<String, Double>> getMrAlemaari() {
        return mrAlemaari;
    }

    public void setMrAlemaari(List<Entry<String, Double>> mrAlemaari) {
        this.mrAlemaari = mrAlemaari;
    }

    public List<Entry<String, Double>> getMsAlemaari() {
        return msAlemaari;
    }

    public void setMsAlemaari(List<Entry<String, Double>> msAlemaari) {
        this.msAlemaari = msAlemaari;
    }

    public AthleteProfile getAthleteProfile() {
        return athleteProfile;
    }

    public void setAthleteProfile(AthleteProfile athleteProfile) {
        this.athleteProfile = athleteProfile;
    }

}
