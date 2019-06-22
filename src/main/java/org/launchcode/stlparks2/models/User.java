package org.launchcode.stlparks2.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDate;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;


    private String userName;

    @NotNull
    private String hashedPassword;

    private LocalDate joinDate;

    public User(String userName, String password){
        this.userName = userName;
        this.hashedPassword = getSHA256(password);
        this.joinDate = LocalDate.now();
    }

    public User(){}

    public  String getSHA256(String input){

        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    public int getId() {
        return id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String password) {
        this.hashedPassword = getSHA256(password);
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

}