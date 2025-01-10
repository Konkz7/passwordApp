package com.example.password.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "folder_table")

public class FolderData {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Folder ID")
    private Long fid;

    @NonNull
    @ColumnInfo(name = "User ID")
    private Long uid;

    @NonNull
    @ColumnInfo(name = "Folder Name")
    private String folderName;


    public FolderData(@NonNull Long fid, @NonNull Long uid, String folderName) {
        this.fid = fid;
        this.uid = uid;
        this.folderName = folderName;
    }

    @NonNull
    public String getFolderName() {
        return folderName;
    }

    @NonNull
    public Long getFid() {
        return fid;
    }


    @NonNull
    public Long getUid() {
        return uid;
    }
}