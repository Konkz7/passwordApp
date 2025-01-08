package com.example.password.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sign_table")

public class SignData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "User ID")
    private Long id;

    @NonNull
    @ColumnInfo(name = "Email")
    private String email;

    @NonNull
    @ColumnInfo(name = "Password")
    private String password;

    @NonNull
    @ColumnInfo(name = "Salt")
    private String salt;

    @NonNull
    @ColumnInfo(name = "PhoneNumber")
    private String phoneNumber;

    @NonNull
    @ColumnInfo(name = "Location")
    private String location;

    public SignData(@NonNull Long id, @NonNull String email, @NonNull String password, @NonNull String salt, @NonNull String phoneNumber,@NonNull String location ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @NonNull
    public String getSalt() {
        return salt;
    }

    public void setSalt(@NonNull String salt) {
        this.salt = salt;
    }
}
