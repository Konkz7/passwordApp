package com.example.password.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.password.Entities.FolderData;

import java.util.List;

@Dao
public interface FolderDao {

    // Insert a folder (no user-specific constraint here)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FolderData folderData);

    // Delete all folders for a specific user
    @Query("DELETE FROM folder_table WHERE `User ID` = :uid")
    void deleteAll(Long uid);

    // Delete a specific folder by ID for a specific user
    @Query("DELETE FROM folder_table WHERE `Folder ID` = :fid AND `User ID` = :uid")
    void deleteFolder(Long fid, Long uid);

    // Rename a folder for a specific user
    @Query("UPDATE folder_table SET `Folder Name` = :name WHERE `Folder ID` = :fid AND `User ID` = :uid")
    void renameFolder(Long fid, String name, Long uid);

    // Get a folder by ID for a specific user
    @Query("SELECT * FROM folder_table WHERE `Folder ID` = :fid AND `User ID` = :uid")
    FolderData getFolder(Long fid, Long uid);

    // Get all folders for a specific user
    @Query("SELECT * FROM folder_table WHERE `User ID` = :uid")
    List<FolderData> getAllFolders(Long uid);

    // Get all folders for a specific user as LiveData
    @Query("SELECT * FROM folder_table WHERE `User ID` = :uid")
    LiveData<List<FolderData>> getAllFoldersLive(Long uid);
}
