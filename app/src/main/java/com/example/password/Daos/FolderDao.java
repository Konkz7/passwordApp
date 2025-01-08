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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(FolderData folderData);

    @Query("DELETE FROM folder_table")
    void deleteAll();

    @Query("DELETE FROM folder_table WHERE `Folder ID` = :fid ")
    void deleteFolder(Long fid);

    @Query("UPDATE folder_table SET `Folder Name` = :name WHERE `Folder ID` = :fid")
    void renameFolder(Long fid,String name);
    @Query("SELECT * FROM folder_table WHERE `Folder ID` = :fid ")
    FolderData getFolder(Long fid);
    @Query("SELECT * FROM folder_table")
    List<FolderData> getAllFolders();

    @Query("SELECT * FROM folder_table ")
    LiveData<List<FolderData>> getAllFoldersLive();


}
