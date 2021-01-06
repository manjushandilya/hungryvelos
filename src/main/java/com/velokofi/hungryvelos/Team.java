package com.velokofi.hungryvelos;

import java.util.List;

public class Team {

    private int id;

    private String name;

    private long captainId;

    private List<TeamMember> members;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCaptainId() {
        return captainId;
    }

    public void setCaptainId(long captainId) {
        this.captainId = captainId;
    }

    public List<TeamMember> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMember> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", captainId=" + captainId +
                '}';
    }
}