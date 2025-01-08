package com.example.password.Models;

import com.example.password.Daos.FolderDao;
import com.example.password.Daos.PasswordDao;
import com.example.password.Daos.SignDao;
import com.example.password.Entities.FolderData;
import com.example.password.Entities.PasswordData;
import com.example.password.Entities.SignData;

import java.util.List;

import javax.crypto.SecretKey;

public class Repo {

    private MainDatabase db;
    private SignDao signDao;

    private PasswordDao passwordDao;
    private FolderDao folderDao;

    private List<PasswordData> filtered;

    private List<FolderData> folders;

    private SignData currentUser;

    private SecretKey key;

    protected static Repo repo = new Repo();


    public Repo() {
    }

    public SignDao getSignDao() {
        return signDao;
    }

    public void setSignDao(SignDao signDao) {
        this.signDao = signDao;
    }

    public MainDatabase getDb() {
        return db;
    }

    public void setDb(MainDatabase db) {
        this.db = db;
    }

    public PasswordDao getPasswordDao() {
        return passwordDao;
    }

    public void setPasswordDao(PasswordDao passwordDao) {
        this.passwordDao = passwordDao;
    }

    public FolderDao getFolderDao() {
        return folderDao;
    }

    public void setFolderDao(FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    public List<FolderData> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderData> folders) {
        this.folders = folders;
    }

    public List<PasswordData> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<PasswordData> filtered) {
        this.filtered = filtered;
    }


    public SignData getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(SignData currentUser) {
        this.currentUser = currentUser;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }
}
