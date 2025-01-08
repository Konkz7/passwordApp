package com.example.password.Daos;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.password.Entities.PasswordData;

import java.util.Date;
import java.util.List;

@Dao
public interface PasswordDao {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insert(PasswordData passwordData);

        @Query("DELETE FROM password_table")
        void deleteAll();

        @Query("DELETE FROM password_table WHERE `Password ID` = :pid")
        void deletePassword(Long pid);

        @Query("SELECT * FROM password_table")
        List<PasswordData> getAllPasswordData();

        @Query("SELECT * FROM password_table WHERE Redacted = :redacted")
        List<PasswordData> getAllPasswordData(boolean redacted);

        @Query("SELECT * FROM password_table WHERE Redacted = :redacted AND `Folder ID` = :fid")
        List<PasswordData> getFilteredPasswordData(Long fid,boolean redacted);

        @Query("SELECT * FROM password_table WHERE Redacted = :redacted")
        LiveData<List<PasswordData>> getAllPasswordDataLive(boolean redacted);

        @Query("SELECT * FROM password_table WHERE Redacted = :redacted ")
        Cursor getAllPasswordDataCursor(boolean redacted);

        @Query("UPDATE password_table SET `Folder ID` = :fid WHERE `Password ID` = :pid")
        void reFolderPassword(Long pid, Long fid);

        @Query("UPDATE password_table SET `App Name` = :appname, Username = :username , Password = :password , `Valid Period` = :renewal , `Last Changed` = :lastChanged WHERE `Password ID` = :pid")
        void changePassword(Long pid, String appname, String username, String password, int renewal, Date lastChanged);

        @Query("UPDATE password_table SET Redacted = :redacted  WHERE `Password ID` = :pid")
        void changePasswordValidity(Long pid, boolean redacted);



}
