package com.example.todolist.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userId")
    private String userId;

    @ColumnInfo(name = "user_name")
    @NonNull
    private String userName;

    @ColumnInfo(name = "password")
    @NonNull
    private String password;

    @ColumnInfo(name = "full_name")
    @NonNull
    private String fullName;

    @ColumnInfo(name = "gender")
    @NonNull
    private String gender;

    public User(String userId, String userName, String password, String fullName, String gender) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

