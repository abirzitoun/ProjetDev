package com.example.formPatient.Service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.formPatient.Entity.User;

@Dao
public interface UserDao {


    @Update
    void updateUser(User user);

    @Insert
    void insertUser(User user);
}
