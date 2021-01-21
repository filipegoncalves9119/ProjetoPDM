package com.example.medicacaocrianca.dbobjects;

import java.util.List;

public class Room {

    private String teacher;
    private List<Children> list;
    private int roomNumber;

    public Room(String teacher,List<Children> list, int roomNumber){
        this.teacher = teacher;
        this.list = list;
        this.roomNumber = roomNumber;
    }

    public Room(){

    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public List<Children> getList() {
        return list;
    }

    public void setList(List<Children> list) {
        this.list = list;
    }

    public int getNumber() {
        return roomNumber;
    }

    public void setNumber(int number) {
        this.roomNumber = number;
    }
}
