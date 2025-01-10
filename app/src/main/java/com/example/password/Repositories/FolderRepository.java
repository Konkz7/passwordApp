package com.example.password.Repositories;

import androidx.lifecycle.LiveData;

import com.example.password.Daos.FolderDao;
import com.example.password.Entities.FolderData;

import java.util.List;

public class FolderRepository {

    private final FolderDao folderDao;
    private final Long UID; // User ID or unique identifier

    public FolderRepository(FolderDao folderDao, Long UID) {
        this.folderDao = folderDao;
        this.UID = UID;
    }

    // Insert a folder
    public void insert_Folder(FolderData folderData) {
        folderDao.insert(folderData);
    }

    // Delete all folders (optionally scoped by UID if needed)
    public void delete_All() {
        folderDao.deleteAll(UID);
    }

    // Delete a specific folder by ID
    public void delete_Folder(Long folderId) {
        folderDao.deleteFolder(folderId,UID);
    }

    // Rename a folder
    public void rename_Folder(Long folderId, String newName) {
        folderDao.renameFolder(folderId, newName,UID);
    }

    // Get a folder by ID
    public FolderData get_Folder(Long folderId) {
        return folderDao.getFolder(folderId,UID);
    }

    // Get all folders
    public List<FolderData> get_All_Folders() {
        return folderDao.getAllFolders(UID);
    }

    // Get all folders as LiveData
    public LiveData<List<FolderData>> get_All_Folders_Live() {
        return folderDao.getAllFoldersLive(UID);
    }

}
