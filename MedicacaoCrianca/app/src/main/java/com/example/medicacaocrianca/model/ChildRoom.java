package com.example.medicacaocrianca.model;

public class ChildRoom {

    private int roomNumber;
    private String name;

    public ChildRoom(Integer roomNumber, String name){
        this.roomNumber = roomNumber;
        this.name = name;
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
}
