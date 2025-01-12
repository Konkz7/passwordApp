package com.example.password.Models;


import static com.example.password.Models.ModelRepository.repo;


import android.app.Activity;
import android.content.Context;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.password.Daos.SignDao;
import com.example.password.Encryptor;
import com.example.password.Entities.SignData;
import com.example.password.Repositories.FolderRepository;
import com.example.password.Repositories.PasswordRepository;
import com.example.password.Repositories.SignRepository;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class LogModel extends ViewModel {


    public LogModel() {

    }
    public void initDatabase(Context context) {
        repo.setDb(MainDatabase.getDatabase(context));
        repo.setSignRepo(repo.getDb().signRepo);

        //repo.setRecords(repo.getPointDao().getAllDataID(false));
    }

    public SignRepository getSignRepo(){
        return repo.getSignRepo();
    }
    public PasswordRepository getPasswordRepo(){
        return repo.getPasswordRepo();
    }
    public FolderRepository getFolderRepo(){
        return repo.getFolderRepo();
    }



    public SignData getCurrentUser(){
        return repo.getCurrentUser();
    }

    public void setCurrentID(SignData currentUser) {
        repo.setCurrentUser(currentUser);
        repo.getDb().initRepo(currentUser.getId());
        repo.setPasswordRepo(repo.getDb().passwordRepo);
        repo.setFolderRepo(repo.getDb().folderRepo);
    }

    public boolean validateLogin(String email, String password) throws Exception {


        SignData temp = getSignRepo().get_User_By_Email(email);

        if (temp == null){
            return false;
        }

        String PW = temp.getSalt() + password;
        PW = Encryptor.hashString(PW);

        if(PW.equals(temp.getPassword())){
            repo.setKey(Encryptor.generateKeyFromPassword(password,temp.getSalt().getBytes(StandardCharsets.UTF_8)));
            return true;
        }else{
            return false;
        }

    }

    public void addLogin(String email,String password,String phoneNumber) throws NoSuchAlgorithmException {

        SignData example = getSignRepo().get_User_By_Email(email);

        if(example != null){
            Log.e("Error :",("An account with this email already exists"));
            return;
        }

        String salt = Encryptor.generateSalt();
        password = Encryptor.hashString(salt + password);
        String finalPassword = password;

        MainDatabase.databaseWriteExecutor.execute(() -> {
            getSignRepo().insert_Sign(new SignData(null, email, finalPassword, salt, phoneNumber, "N/A"));
        });

    }
    public void deleteAccount(Activity activity){
        activity.deleteSharedPreferences(getCurrentUser().getId()+ "AppPrefs");
        getPasswordRepo().delete_All();
        getFolderRepo().delete_All();
        getSignRepo().delete_Sign(getCurrentUser().getId());
    }

}
