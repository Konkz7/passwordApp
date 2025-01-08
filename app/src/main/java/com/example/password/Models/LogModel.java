package com.example.password.Models;

import static com.example.password.Models.Repo.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.password.Daos.SignDao;
import com.example.password.Encryptor;
import com.example.password.Entities.SignData;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class LogModel extends ViewModel {


    public LogModel() {

    }

    public void initDatabase(Context context) {
        repo.setDb(MainDatabase.getDatabase(context));
        repo.setSignDao(repo.getDb().signDao());
        repo.setPasswordDao(repo.getDb().passwordDao());
        repo.setFolderDao(repo.getDb().folderDao());
        //repo.setRecords(repo.getPointDao().getAllDataID(false));
    }

    public SignDao getSignDao(){
        return repo.getSignDao();
    }


    public SignData getCurrentUser(){
        return repo.getCurrentUser();
    }

    public void setCurrentID(SignData currentUser) {
         repo.setCurrentUser(currentUser);
    }

    public boolean validateLogin(String email, String password) throws Exception {

        SignData temp = getSignDao().getUserByEmail(email);

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

        SignData example = getSignDao().getUserByEmail(email);

        if(example != null){
            Log.e("Error :",("An account with this email already exists"));
            return;
        }

        String salt = Encryptor.generateSalt();
        password = Encryptor.hashString(salt + password);
        String finalPassword = password;

        MainDatabase.databaseWriteExecutor.execute(() -> {
            getSignDao().insert(new SignData(null, email, finalPassword, salt, phoneNumber, "N/A"));
        });

    }

}
