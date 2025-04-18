package com.example.password.Daos;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.password.Entities.SignData;

import java.util.List;

@Dao
public interface SignDao {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insert(SignData signData);

        @Query("DELETE FROM sign_table")
        void deleteAll();

        @Query("SELECT * FROM sign_table")
        Cursor getAllSignUpDataCursor();

        @Query("SELECT * FROM sign_table")
        LiveData<List<SignData>> getAllSignUpDataLive();

        @Query("SELECT * FROM sign_table")
        List<SignData> getAllSignUpData();

        @Query("SELECT * FROM sign_table WHERE Email = :email")
        SignData getUserByEmail(String email);

}


