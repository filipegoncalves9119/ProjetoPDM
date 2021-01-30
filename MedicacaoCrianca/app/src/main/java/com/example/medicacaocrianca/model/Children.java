package com.example.medicacaocrianca.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormat;

@Entity
public class Children {

    @PrimaryKey(autoGenerate = true)
    private  long id;

    private String fullName;
    private String address;
    private String birthDate;
    private String phoneNumber;
    private String parent;
    private String uri;
    private String pills;
    private String time;

    /**
     * Class constructor
     * @param fullName
     * @param address
     * @param birthDate
     * @param phoneNumber
     * @param parent
     */
    @Ignore
    public Children(String fullName, String address, String birthDate, String phoneNumber, String parent, String uri) {
        this.fullName = fullName;
        this.address = address;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.parent = parent;
        this.uri = uri;
    }


    /**
     * Class Constructor
     */
    public Children(){

    }


    //Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPills() {
        return pills;
    }

    public void setPills(String pills) {
        this.pills = pills;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
