package com.example.password.Models;

import androidx.lifecycle.ViewModel;

public class RegModel extends ViewModel {
    private String email;
    private String number;



    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
