package org.launchcode.stlparks2.models.forms;

import org.launchcode.stlparks2.models.Park;
import org.launchcode.stlparks2.models.User;

public class AddParkToProfileForm {

    private User user;

    private Iterable<Park> parks;

    private int parkId;

    private int userId;

    public AddParkToProfileForm() {
    }

    public AddParkToProfileForm(User user, Iterable<Park> parks) {
        this.user = user;
        this.parks = parks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Iterable<Park> getParks() {
        return parks;
    }

    public void setParks(Iterable<Park> parks) {
        this.parks = parks;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}


