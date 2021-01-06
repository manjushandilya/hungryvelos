package com.velokofi.hungryvelos;

/*
    {
        "resource_state": 2,
        "firstname": "Ravi",
        "lastname": "S."
    },
*/
public class ClubActivityAthlete {
    private int resource_state;
    private String firstname;
    private String lastname;

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

    public String getAthleteName() {
        return firstname + " " + lastname;
    }

    @Override
    public String toString() {
        return "Athlete{" +
                "resource_state=" + resource_state +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClubActivityAthlete clubActivityAthlete = (ClubActivityAthlete) o;

        if (!firstname.equals(clubActivityAthlete.firstname)) return false;
        return lastname.equals(clubActivityAthlete.lastname);
    }

    @Override
    public int hashCode() {
        int result = firstname.hashCode();
        result = 31 * result + lastname.hashCode();
        return result;
    }
}
