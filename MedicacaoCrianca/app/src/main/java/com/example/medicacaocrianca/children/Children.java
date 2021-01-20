package com.example.medicacaocrianca.children;

public class Children {

    private String fullName;
    private String address;
    private String birthDate;
    private String phoneNumber;
    private String parent;

    public Children(String fullName, String address, String birthDate, String phoneNumber, String parent) {
        this.fullName = fullName;
        this.address = address;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.parent = parent;
    }

    public Children(){

    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getParent() {
        return parent;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
