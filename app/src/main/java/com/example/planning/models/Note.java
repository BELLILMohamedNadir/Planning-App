package com.example.planning.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan_table",
        primaryKeys = {"date", "time"},
        foreignKeys = @ForeignKey(
                entity = Information.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "userId")}
)
public class Note implements Parcelable {
    @NonNull
    private String date;
    @NonNull
    private String time;
    private String title;
    private String description;

    private int userId;

    public Note() {
    }

    public Note(@NonNull String date, @NonNull String time, String title, String description, int userId) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    protected Note(Parcel in) {
        date = in.readString();
        time = in.readString();
        title = in.readString();
        description = in.readString();
        userId = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(userId);
    }
}
