package com.example.medicacaocrianca.database;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.medicacaocrianca.model.Children;

@Database(entities = {Children.class}, version = 2, exportSchema = false)
public abstract class ChildrenDatabase extends RoomDatabase {

    private static ChildrenDatabase instance;

    public static ChildrenDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, ChildrenDatabase.class, "children_db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract ChildrenDao childrenDao();

}
