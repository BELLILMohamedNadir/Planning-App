package com.example.planning.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.planning.data_base.InfosDao;
import com.example.planning.data_base.MyDataBase;
import com.example.planning.models.Information;
import com.example.planning.models.Note;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Repository {

    private InfosDao infosDao;
    private final MutableLiveData<Long> liveDataInsertedInformation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveDataInsertedNote = new MutableLiveData<>();
    private final MutableLiveData<Boolean> liveDataDeleteNote = new MutableLiveData<>();
    private final MutableLiveData<Information> liveDataGetInformation = new MutableLiveData<>();
    private final MutableLiveData<List<Note>> liveDataGetNotes = new MutableLiveData<>();
    private final MutableLiveData<List<Note>> liveDataGetAllNotes = new MutableLiveData<>();
    private final MutableLiveData<String> liveDataLogin = new MutableLiveData<>();
    private final MutableLiveData<Long> liveDataCheckLogin = new MutableLiveData<>();
    private static final String TAG = "Repository";
    public static volatile Repository INSTANCE=null;
    public Repository(Application application){
        MyDataBase dataBase=MyDataBase.getInstance(application.getApplicationContext());
        infosDao=dataBase.infosDao();
    }
    public static Repository getInstance(Application application){
        if (INSTANCE==null){
            synchronized (DataViewModel.class){
                if (INSTANCE==null){
                    INSTANCE=new Repository(application);
                }
            }
        }
        return INSTANCE;
    }
    public void insertInformation(Information information){
        infosDao.insertInformation(information).subscribeOn(Schedulers.computation())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "insertInformation onSubscribe ");
                    }

                    @Override
                    public void onSuccess(Long id) {
                        Log.d(TAG, "insertInformation onComplete: info "+id);
                        liveDataInsertedInformation.postValue(id);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "insertInformation onError: info "+e.getMessage());
                    }
                });
    }

    public void insertNote(Note note){
        infosDao.insertNote(note).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "insertNote onComplete: info ");
                        liveDataInsertedNote.postValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "insertNote onError: info "+e.getMessage());
                        liveDataInsertedNote.postValue(false);
                    }
                });
    }

    public void deleteNote(Note note){
        infosDao.deleteNote(note).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "deleteNote onComplete: info ");
                        liveDataDeleteNote.postValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "deleteNote onError: info "+e.getMessage());
                        liveDataDeleteNote.postValue(false);
                    }
                });
    }

    public void getInformation (long id){
        infosDao.getInformation(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Information>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Information information) {
                        Log.d(TAG, "getInformation onNext: info "+information);
                        liveDataGetInformation.postValue(information);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getInformation onError: info "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getLogin(String login) {
        infosDao.getLogin(login).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "getLogin subscribe: ");
                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.d(TAG, "getLogin onSuccess: " + s);
                        liveDataLogin.postValue(s); // Post the existing login
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getLogin onError: ", e);
                        liveDataLogin.postValue(null); // Consider error as non-existing
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "getLogin onComplete: No login found");
                        liveDataLogin.postValue(""); // Post empty string for non-existing
                    }
                });
    }

    public void checkLogin (String login, String password){
        infosDao.checkLogin(login, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long id) {
                        liveDataCheckLogin.postValue(id);
                        Log.d(TAG, "checkLogin onNext: "+id);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "checkLogin onError: info "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        liveDataCheckLogin.postValue(-1L);
                    }
                });
    }

    public void getNotes (long id, String date){
        infosDao.getNotes(id, date).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Note> notes) {
                        Log.d(TAG, "getNotes onNext: info ");
                        liveDataGetNotes.postValue(notes);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getNotes onError: info "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getAllNotes (){
        infosDao.getAllNotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Note> notes) {
                        Log.d(TAG, "getAllNotes onNext: info ");
                        liveDataGetAllNotes.postValue(notes);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getAllNotes onError: info "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<Long> getLiveDataInsertedInformation() {
        return liveDataInsertedInformation;
    }

    public MutableLiveData<Information> getLiveDataGetInformation() {
        return liveDataGetInformation;
    }

    public MutableLiveData<Long> getLiveDataCheckLogin() {
        return liveDataCheckLogin;
    }

    public MutableLiveData<String> getLiveDataLogin() {
        return liveDataLogin;
    }

    public void reinitialiseLiveDataLogin(){
        this.liveDataLogin.setValue(null);
    }

    public void reinitialiseLiveDataInsertedNote(){
        this.liveDataInsertedNote.setValue(null);
    }

    public void reinitialiseLiveDataDeleteNote(){
        this.liveDataDeleteNote.setValue(null);
    }

    public void reinitialiseLiveDataGetNote(){
        this.getLiveDataGetNotes().setValue(null);
    }

    public void reinitialiseLiveDataInsertedInformation(){
        this.liveDataInsertedInformation.setValue(-1L);
    }

    public void reinitialiseLiveDataGetInformation(){
        this.getLiveDataGetInformation().setValue(null);
    }

    public MutableLiveData<Boolean> getLiveDataInsertedNote() {
        return liveDataInsertedNote;
    }

    public void removeLiveDataInsertedNoteObserver(LifecycleOwner lifecycleOwner) {
        liveDataInsertedNote.removeObservers(lifecycleOwner);
    }

    public MutableLiveData<List<Note>> getLiveDataGetNotes() {
        return liveDataGetNotes;
    }

    public MutableLiveData<List<Note>> getLiveDataGetAllNotes() {
        return liveDataGetAllNotes;
    }

    public MutableLiveData<Boolean> getLiveDataDeleteNote() {
        return liveDataDeleteNote;
    }
}
