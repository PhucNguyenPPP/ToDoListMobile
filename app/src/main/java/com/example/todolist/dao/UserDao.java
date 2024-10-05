package com.example.todolist.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE userId in (:userId)")
    User loadPersonById(String userId);

    @Query("SELECT * FROM user WHERE user_name = :userName")
    User loadPersonByUserName(String userName);

    @Insert
    void insert(User user);

    @Update
    void update (User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAllUsers();

    @Query("SELECT COUNT(*) > 0 FROM User WHERE user_name = :username")
    boolean userExist (String username);

    @Query("SELECT COUNT(*) > 0 FROM User WHERE user_name = :username AND password = :password")
    boolean checkLogin (String username, String password);
}
