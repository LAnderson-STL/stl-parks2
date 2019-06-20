package org.launchcode.stlparks2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Admin {

    @Id
    @GeneratedValue
    private int id;


    private String userName;
    private String password;

    //constructors
    public Admin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Admin(){}


    //getters & setters


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}