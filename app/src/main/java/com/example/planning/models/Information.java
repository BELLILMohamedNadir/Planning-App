package com.example.planning.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "information_table")
public class Information {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String birthday;
    private String numberPhone;
    private String email;
    private boolean sport;
    private boolean music;
    private boolean lecture;

    public Information() {
    }

    public Information(int id, String login, String password, String name, String lastName, String birthday, String numberPhone, String email, boolean sport, boolean music, boolean lecture) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.numberPhone = numberPhone;
        this.email = email;
        this.sport = sport;
        this.music = music;
        this.lecture = lecture;
    }

    public Information(String login, String password, String name, String lastName, String birthday, String numberPhone, String email, boolean sport, boolean music, boolean lecture) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.numberPhone = numberPhone;
        this.email = email;
        this.sport = sport;
        this.music = music;
        this.lecture = lecture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSport() {
        return sport;
    }

    public void setSport(boolean sport) {
        this.sport = sport;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isLecture() {
        return lecture;
    }

    public void setLecture(boolean lecture) {
        this.lecture = lecture;
    }

    @Override
    public String toString() {
        return "Information{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", numberPhone='" + numberPhone + '\'' +
                ", email='" + email + '\'' +
                ", sport=" + sport +
                ", music=" + music +
                ", lecture=" + lecture +
                '}';
    }
}
