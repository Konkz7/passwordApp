package com.example.password.Models;

import com.example.password.Daos.FolderDao;
import com.example.password.Daos.PasswordDao;
import com.example.password.Daos.SignDao;
import com.example.password.Entities.FolderData;
import com.example.password.Entities.PasswordData;
import com.example.password.Entities.SignData;
import com.example.password.Repositories.FolderRepository;
import com.example.password.Repositories.PasswordRepository;
import com.example.password.Repositories.SignRepository;

import java.util.List;

import javax.crypto.SecretKey;

public class ModelRepository {

    private MainDatabase db;
    private SignRepository signRepo;

    private PasswordRepository passwordRepo;
    private FolderRepository folderRepo;

    private List<PasswordData> filtered;

    private List<FolderData> folders;

    private SignData currentUser;


    protected static ModelRepository repo = new ModelRepository();


    public ModelRepository() {
    }


    public MainDatabase getDb() {
        return db;
    }

    public void setDb(MainDatabase db) {
        this.db = db;
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



    public PasswordRepository getPasswordRepo() {
        return passwordRepo;
    }

    public void setPasswordRepo(PasswordRepository passwordRepo) {
        this.passwordRepo = passwordRepo;
    }

    public FolderRepository getFolderRepo() {
        return folderRepo;
    }

    public void setFolderRepo(FolderRepository folderRepo) {
        this.folderRepo = folderRepo;
    }

    public SignRepository getSignRepo() {
        return signRepo;
    }

    public void setSignRepo(SignRepository signRepo) {
        this.signRepo = signRepo;
    }
}
