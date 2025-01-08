package com.example.password.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "password_table")

public class PasswordData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Password ID")
    private Long pid;

    @NonNull
    @ColumnInfo(name = "Password")
    private String password;

    @NonNull
    @ColumnInfo(name = "App Name")
    private String appName;


    @ColumnInfo(name = "Username")
    private String userName;

    @NonNull
    @ColumnInfo(name = "Last Changed")
    private Date lastChanged;


    @ColumnInfo(name = "Folder ID")
    private Long fid;

    @NonNull
    @ColumnInfo(name = "User ID")
    private Long id;

    @NonNull
    @ColumnInfo(name = "Redacted")
    private boolean redacted;

    @NonNull
    @ColumnInfo(name = "Valid Period")
    private int renewal;


    public PasswordData(Long pid, String password, String appName, String userName,
                        Date lastChanged, Long fid, Long id, boolean redacted, int renewal) {
        this.pid = pid;
        this.lastChanged = lastChanged;
        this.password = password;
        this.appName = appName;
        this.fid = fid;
        this.id = id;
        this.redacted = redacted;
        this.userName = userName;
        this.renewal = renewal;

    }

    public Long getPid() {
        return pid;
    }
    public String getPassword() {
        return password;
    }
    public String getAppName() {
        return appName;
    }
    @NonNull
    public Date getLastChanged() {
        return lastChanged;
    }
    public Long getFid() {
        return fid;
    }
    public Long getId() {
        return id;
    }
    public boolean isRedacted() {
        return redacted;
    }
    public String getUserName() {
        return userName;
    }
    public int getRenewal() {
        return renewal;
    }

}
