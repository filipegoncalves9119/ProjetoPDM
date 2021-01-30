package com.example.medicacaocrianca.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medicacaocrianca.model.Children;

@Dao
public interface ChildrenDao {

    @Insert
    long inset(Children children);

    @Update
    void update(Children update);

    @Query("select * from children where id = :id")
    Children get(long id);

    //@Query("select * from children where fullName = :name");
    //Children get(String name);
}
