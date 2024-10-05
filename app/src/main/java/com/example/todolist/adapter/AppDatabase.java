package com.example.todolist.adapter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todolist.dao.TaskDao;
import com.example.todolist.dao.UserDao;
import com.example.todolist.model.Task;
import com.example.todolist.model.User;

@Database(entities = {User.class, Task.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TaskDao taskDao();
}
