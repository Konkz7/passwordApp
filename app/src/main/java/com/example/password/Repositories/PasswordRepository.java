package com.example.password.Repositories;

import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.example.password.Daos.PasswordDao;
import com.example.password.Entities.PasswordData;

import java.util.Date;
import java.util.List;

public class PasswordRepository {

    private final PasswordDao passwordDao;
    private final Long UID; // User ID

    // Constructor
    public PasswordRepository(PasswordDao passwordDao, Long UID) {
        this.passwordDao = passwordDao;
        this.UID = UID;
    }

    // Insert a new password
    public void insert_Password(PasswordData passwordData) {
        passwordDao.insert(passwordData);
    }

    // Delete all passwords for the current user
    public void delete_All() {
        passwordDao.deleteAll(UID);
    }

    // Delete a specific password by ID for the current user
    public void delete_Password(Long passwordId) {
        passwordDao.deletePassword(passwordId, UID);
    }

    // Get all password data for the current user
    public List<PasswordData> get_All_Password_Data() {
        return passwordDao.getAllPasswordData(UID);
    }

    // Get all password data for the current user with redaction status
    public List<PasswordData> get_All_Password_Data(boolean redacted) {
        return passwordDao.getAllPasswordData(UID, redacted);
    }

    // Get filtered password data for the current user, folder, and redaction status
    public List<PasswordData> get_Filtered_Password_Data(Long folderId, boolean redacted) {
        return passwordDao.getFilteredPasswordData(UID, folderId, redacted);
    }

    // Get all password data as LiveData for the current user with redaction status
    public LiveData<List<PasswordData>> get_All_Password_Data_Live(boolean redacted) {
        return passwordDao.getAllPasswordDataLive(UID, redacted);
    }

    // Refolder a password for the current user
    public void re_Folder_Password(Long passwordId, Long folderId) {
        passwordDao.reFolderPassword(passwordId, folderId, UID);
    }

    // Change password details for the current user
    public void change_Password(Long passwordId, String appName, String username, String password, int renewal, Date lastChanged) {
        passwordDao.changePassword(passwordId, appName, username, password, renewal, lastChanged, UID);
    }

    // Change password validity for the current user
    public void change_Password_Validity(Long passwordId, boolean redacted) {
        passwordDao.changePasswordValidity(passwordId, redacted, UID);
    }
}
