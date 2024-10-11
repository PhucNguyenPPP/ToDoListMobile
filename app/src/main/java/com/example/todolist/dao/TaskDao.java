package com.example.todolist.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE userId = (:userId)")
    Task loadTaskByUserId(String userId);

    @Query("SELECT * FROM task WHERE userId = :userId")
    List<Task> getTasksByUserId(String userId);

    // Lấy tổng số task của người dùng
    @Query("SELECT COUNT(*) FROM task WHERE userId = :userId")
    LiveData<Integer> getTotalTaskCount(String userId);

    // Lấy số lượng task đã hoàn thành của người dùng
    @Query("SELECT COUNT(*) FROM task WHERE userId = :userId AND is_completed = 1")
    LiveData<Integer> getCompletedTaskCount(String userId);

    // Lấy số lượng task chưa hoàn thành của người dùng
    @Query("SELECT COUNT(*) FROM task WHERE userId = :userId AND is_completed = 0")
    LiveData<Integer> getIncompleteTaskCount(String userId);

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
