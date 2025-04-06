package com.example.planning.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.planning.models.Information;
import com.example.planning.models.Note;

import java.util.List;


public class DataViewModel extends AndroidViewModel {
    Repository repository;
    public DataViewModel(@NonNull Application application) {
        super(application);
        repository=Repository.getInstance(application);
    }

    public void insertInformation(Information information){
        repository.insertInformation(information);
    }

    public void insertNote(Note note){
        repository.insertNote(note);
    }

    public void getInformation(long id){
        repository.getInformation(id);
    }

    public void getNotes(long userId, String date){
        repository.getNotes(userId, date);
    }

    public void getLogin(String login){
        repository.getLogin(login);
    }

    public void deleteNote(Note note){
        repository.deleteNote(note);
    }

    public void checkLogin(String login, String password){
        repository.checkLogin(login, password);
    }

    public LiveData<Long> getLiveDataInsertedInformation(){
        return repository.getLiveDataInsertedInformation();
    }

    public LiveData<Information> getLiveDataGetInformation(){
        return repository.getLiveDataGetInformation();
    }

    public LiveData<String> getLiveDataGetLogin(){
        return repository.getLiveDataLogin();
    }

    public LiveData<Long> getLiveDataCheckLogin(){
        return repository.getLiveDataCheckLogin();
    }

    public LiveData<Boolean> getLiveDataDeleteNote(){
        return repository.getLiveDataDeleteNote();
    }

    public LiveData<Boolean> getLiveDataInsertedNote(){
        return repository.getLiveDataInsertedNote();
    }

    public LiveData<List<Note>> getLiveDataGetNotes(){
        return repository.getLiveDataGetNotes();
    }

    public void reinitialiseLiveDataGetLogin(){
        repository.reinitialiseLiveDataLogin();
    }

    public void reinitialiseLiveDataGetInformation(){
        repository.reinitialiseLiveDataGetInformation();
    }

    public void reinitialiseLiveDataInsertedNote(){
        repository.reinitialiseLiveDataInsertedNote();
    }
    public void reinitialiseLiveDataInsertedInformation(){
        repository.reinitialiseLiveDataInsertedInformation();
    }

    public void reinitialiseLiveDataGetNote(){
        repository.reinitialiseLiveDataGetNote();
    }
    public void reinitialiseLiveDataDeleteNote(){
        repository.reinitialiseLiveDataDeleteNote();
    }
}
