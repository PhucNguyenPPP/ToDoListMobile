package com.example.todolist.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.model.Task;
import com.example.todolist.model.User;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE userId in (:userId)")
    Task loadTaskByUserId(String userId);

    @Insert
    void insert(Task task);

    @Update
    void update (Task task);

    @Delete
    void delete(Task task);

}
