package com.velokofi.hungryvelos.model;

public class TeamMember {

    private long id;

    private String name;

    private String gender;

    private int teamId;

    private boolean captain;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public boolean isCaptain() {
        return captain;
    }

    public void setCaptain(boolean captain) {
        this.captain = captain;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TeamMember{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", teamId=").append(teamId);
        sb.append(", captain=").append(captain);
        sb.append('}');
        return sb.toString();
    }
}
