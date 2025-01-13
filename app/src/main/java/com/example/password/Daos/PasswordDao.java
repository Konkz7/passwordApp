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

        // Insert a password (no user-specific constraint here)
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insert(PasswordData passwordData);

        // Delete all passwords for a specific user
        @Query("DELETE FROM password_table WHERE `User ID` = :uid")
        void deleteAll(Long uid);

        // Delete a specific password by ID for a specific user
        @Query("DELETE FROM password_table WHERE `Password ID` = :pid AND `User ID` = :uid")
        void deletePassword(Long pid, Long uid);

        // Get all password data for a specific user
        @Query("SELECT * FROM password_table WHERE `User ID` = :uid")
        List<PasswordData> getAllPasswordData(Long uid);

        // Get all password data for a specific user and redaction status
        @Query("SELECT * FROM password_table WHERE `User ID` = :uid AND Redacted = :redacted")
        List<PasswordData> getAllPasswordData(Long uid, boolean redacted);

        // Get filtered password data for a specific user, folder, and redaction status
        @Query("SELECT * FROM password_table WHERE `User ID` = :uid AND `Folder ID` = :fid AND Redacted = :redacted")
        List<PasswordData> getFilteredPasswordData(Long uid, Long fid, boolean redacted);

        // Get all password data as LiveData for a specific user and redaction status
        @Query("SELECT * FROM password_table WHERE `User ID` = :uid AND Redacted = :redacted")
        LiveData<List<PasswordData>> getAllPasswordDataLive(Long uid, boolean redacted);

        // Refolder a password for a specific user
        @Query("UPDATE password_table SET `Folder ID` = :fid WHERE `Password ID` = :pid AND `User ID` = :uid")
        void reFolderPassword(Long pid, Long fid, Long uid);

        // Change password details for a specific user
        @Query("UPDATE password_table SET `App Name` = :appname, Username = :username, Password = :password, `Valid Period` = :renewal, `Last Changed` = :lastChanged WHERE `Password ID` = :pid AND `User ID` = :uid")
        void changePassword(Long pid, String appname, String username, String password, int renewal, Date lastChanged, Long uid);

        // Change password validity for a specific user
        @Query("UPDATE password_table SET Redacted = :redacted WHERE `Password ID` = :pid AND `User ID` = :uid")
        void changePasswordValidity(Long pid, boolean redacted, Long uid);

        @Query("UPDATE password_table SET `Last Changed` = :date WHERE `Password ID` = :pid AND `User ID` = :uid")
        void updatePasswordDate(Long pid,Long uid,Date date);
}
