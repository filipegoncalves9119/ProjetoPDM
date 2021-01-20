package com.example.medicacaocrianca.dbobjects;

import java.util.List;

public class Room {

    private Teacher teacher;
    private List<Children> list;
    private int number;

    public Room(Teacher teacher,List<Children> list, int number){
        this.teacher = teacher;
        this.list = list;
        this.number = number;
    }

    public Room(){

    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Children> getList() {
        return list;
    }

    public void setList(List<Children> list) {
        this.list = list;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
