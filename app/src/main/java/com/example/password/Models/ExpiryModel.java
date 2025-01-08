package com.example.password.Models;

import static com.example.password.Models.Repo.repo;

import com.example.password.Daos.PasswordDao;
import com.example.password.Entities.PasswordData;

import java.util.Date;
import java.util.List;

public class ExpiryModel {

    public PasswordDao getPasswordDao(){
        return repo.getPasswordDao();
    }
    private long daysToMilliseconds(int days) {
        // 1 day = 24 hours = 24 * 60 minutes = 24 * 60 * 60 seconds = 24 * 60 * 60 * 1000 milliseconds
        return days * 24 * 60 * 60 * 1000;
    }

    public void expiryFunction(){
        List<PasswordData> Plist = getPasswordDao().getAllPasswordData(false);
        for (PasswordData x: Plist
        ) {

            /*
            if(x.getRenewal() == 0){
                continue;
            }
            */

            if (x.getLastChanged().getTime() + daysToMilliseconds(x.getRenewal()) < new Date().getTime()){
                getPasswordDao().changePasswordValidity(x.getPid(),true);
            }

        }

    }

    public boolean expiryCheck(){
        if(getPasswordDao().getAllPasswordData(true).isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
