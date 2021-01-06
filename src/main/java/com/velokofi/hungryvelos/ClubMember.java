package com.velokofi.hungryvelos;

/*
    {
        "resource_state": 2,
        "firstname": "Ravi",
        "lastname": "S.",
        "membership": "member",
        "admin": false,
        "owner": false
    }
 */

public class ClubMember {
    private int resource_state;
    private String firstname;
    private String lastname;
    private String membership;
    private boolean admin;
    private boolean owner;

    public int getResource_state() {
        return resource_state;
    }

    public void setResource_state(int resource_state) {
        this.resource_state = resource_state;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "ClubMember{" +
                "resource_state=" + resource_state +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", membership='" + membership + '\'' +
                ", admin=" + admin +
                ", owner=" + owner +
                '}';
    }

}
