package com.example.planning.data_base;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.planning.models.Information;
import com.example.planning.models.Note;


@Database(entities = {Information.class, Note.class},version = 7)
public abstract class MyDataBase extends RoomDatabase {
    public abstract InfosDao infosDao();
    public static volatile MyDataBase INSTANCE=null;
    public static MyDataBase getInstance(Context context){
        if (INSTANCE==null){
            synchronized (MyDataBase.class){
                if (INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),MyDataBase.class,"DB_TP_MOBILE")
                           .fallbackToDestructiveMigrationFrom(0, 1, 2, 3, 4, 5, 6).build();
                }
            }
        }
        return INSTANCE;
    }
}
