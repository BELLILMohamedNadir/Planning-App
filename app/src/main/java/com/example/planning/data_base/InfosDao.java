package com.example.planning.data_base;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.planning.models.Information;
import com.example.planning.models.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;


@Dao
public interface InfosDao {
    @Insert
    Single<Long> insertInformation(Information info);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNote(Note note);

    @Delete
    Completable deleteNote(Note note);

    @Query("SELECT * FROM information_table WHERE id = :id")
    Observable<Information> getInformation(long id);

    @Query("SELECT login FROM information_table WHERE login = :login")
    Maybe<String> getLogin(String login);

    @Query("SELECT id FROM information_table WHERE login = :login AND password = :password")
    Maybe<Long> checkLogin(String login, String password);

    @Query("SELECT * FROM plan_table WHERE date = :date AND userId = :userId")
    Observable<List<Note>> getNotes(long userId, String date);

    @Query("SELECT * FROM plan_table")
    Observable<List<Note>> getAllNotes();
}
