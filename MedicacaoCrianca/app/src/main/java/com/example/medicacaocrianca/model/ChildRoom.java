package com.example.medicacaocrianca.model;

public class ChildRoom {

    private int roomNumber;
    private String name;
    private String uri;

    public ChildRoom(Integer roomNumber, String name, String uri){
        this.roomNumber = roomNumber;
        this.name = name;
        this.uri = uri;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
