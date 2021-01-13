package com.velokofi.hungryvelos.model;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LeaderBoard {

    // event totals
    private Double totalDistance;
    private Double totalElevation;
    private int totalRides;
    private long movingTime;
    private Double riderAverage;
    private int riderCount;
    private String movingTimeInHumanReadableFormat;

    // team totals
    private Map<String, Double> teamDistanceMap;
    private Map<String, Double> teamElevationMap;
    private Map<String, Double> teamRidesMap;
    private Map<String, Double> teamSpeedMap;

    // team averages
    private Map<String, Double> teamAvgDistanceMap;
    private Map<String, Double> teamAvgElevationMap;
    private Map<String, Double> teamAvgRidesMap;
    private Map<String, Double> teamAvgSpeedMap;

    // individual totals
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

    public Map<String, Double> getTeamDistanceMap() {
        return teamDistanceMap;
    }

    public void setTeamDistanceMap(Map<String, Double> teamDistanceMap) {
        this.teamDistanceMap = teamDistanceMap;
    }

    public Map<String, Double> getTeamElevationMap() {
        return teamElevationMap;
    }

    public void setTeamElevationMap(Map<String, Double> teamElevationMap) {
        this.teamElevationMap = teamElevationMap;
    }

    public Map<String, Double> getTeamRidesMap() {
        return teamRidesMap;
    }

    public void setTeamRidesMap(Map<String, Double> teamRidesMap) {
        this.teamRidesMap = teamRidesMap;
    }

    public Map<String, Double> getTeamSpeedMap() {
        return teamSpeedMap;
    }

    public void setTeamSpeedMap(Map<String, Double> teamSpeedMap) {
        this.teamSpeedMap = teamSpeedMap;
    }

    public Map<String, Double> getTeamAvgDistanceMap() {
        return teamAvgDistanceMap;
    }

    public void setTeamAvgDistanceMap(Map<String, Double> teamAvgDistanceMap) {
        this.teamAvgDistanceMap = teamAvgDistanceMap;
    }

    public Map<String, Double> getTeamAvgElevationMap() {
        return teamAvgElevationMap;
    }

    public void setTeamAvgElevationMap(Map<String, Double> teamAvgElevationMap) {
        this.teamAvgElevationMap = teamAvgElevationMap;
    }

    public Map<String, Double> getTeamAvgRidesMap() {
        return teamAvgRidesMap;
    }

    public void setTeamAvgRidesMap(Map<String, Double> teamAvgRidesMap) {
        this.teamAvgRidesMap = teamAvgRidesMap;
    }

    public Map<String, Double> getTeamAvgSpeedMap() {
        return teamAvgSpeedMap;
    }

    public void setTeamAvgSpeedMap(Map<String, Double> teamAvgSpeedMap) {
        this.teamAvgSpeedMap = teamAvgSpeedMap;
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
