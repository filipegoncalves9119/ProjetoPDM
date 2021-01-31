package com.example.medicacaocrianca.model;

public class ChildRoom {

    private int roomNumber;
    private String name;
    private String phoneNum;

    public ChildRoom(Integer roomNumber, String name, String phoneNum){
        this.roomNumber = roomNumber;
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public ChildRoom(){

    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
