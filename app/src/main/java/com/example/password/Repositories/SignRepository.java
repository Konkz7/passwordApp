
package com.example.password.Repositories;

import com.example.password.Daos.SignDao;
import com.example.password.Entities.SignData;

import java.util.List;

public class SignRepository {

    private final SignDao signDao;

    // Constructor
    public SignRepository(SignDao signDao) {
        this.signDao = signDao;
    }

    // Insert a new signup data
    public void insert_Sign(SignData signData) {
        signDao.insert(signData);
    }

    public void delete_Sign(Long uid){
        signDao.delete(uid);
    }

    // Retrieve all signup data
    public List<SignData> get_All_Sign_Up_Data() {
        return signDao.getAllSignUpData();
    }

    // Retrieve a user by their email
    public SignData get_User_By_Email(String email) {
        return signDao.getUserByEmail(email);
    }
}
