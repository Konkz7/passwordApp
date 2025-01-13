package com.example.password.Models;

import static com.example.password.Models.ModelRepository.repo;

import com.example.password.Daos.PasswordDao;
import com.example.password.Entities.PasswordData;
import com.example.password.Repositories.PasswordRepository;

import java.util.Date;
import java.util.List;

public class ExpiryModel {

    PasswordRepository passwordRepo;
    ExpiryModel(PasswordRepository passwordRepo){
        this.passwordRepo = passwordRepo;
    }
    public static int daysToMilliseconds(int days) {
        // 1 day = 24 hours = 24 * 60 minutes = 24 * 60 * 60 seconds = 24 * 60 * 60 * 1000 milliseconds
        return days * 24 * 60 * 60 * 1000;
    }

    public static long millisecondsToDays(long milliseconds) {
        long seconds = milliseconds / 1000; // Convert milliseconds to seconds
        // Convert seconds to days
        return seconds / (24 * 60 * 60);
    }


    public void expiryFunction(){

        List<PasswordData> Plist = passwordRepo.get_All_Password_Data(false);
        for (PasswordData x: Plist
        ) {


            if(x.getRenewal() == 0){
                continue;
            }


            if (x.getLastChanged().getTime() + daysToMilliseconds(x.getRenewal()) <= new Date().getTime()){
                passwordRepo.change_Password_Validity(x.getPid(),true);
            }

        }

    }

    public boolean expiryCheck(){
        if(passwordRepo.get_All_Password_Data(true).isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
