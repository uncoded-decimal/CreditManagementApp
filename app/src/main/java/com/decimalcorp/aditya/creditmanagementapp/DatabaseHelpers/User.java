package com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User{

    @PrimaryKey(autoGenerate = false)
    public int uid;

    @ColumnInfo(name = "Username")
    public String name;

    @ColumnInfo(name = "Current Credits")
    public int score;

    @ColumnInfo(name = "Email")
    public String email;

    public User(int i,String name, int score, String email){
        this.name = name;
        this.score = score;
        this.uid = i;
        this.email = email;
    }

    public User(){}

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getEmail() {
        return email;
    }

    public static User[] populate(){
        User[] ar = new User[10];
        for(int i=0; i<10;i++){
            ar[i] = new User(i+1,"USER "+(i+1),100, "user_"+(i+1)+"@mail.com");
        }
        return ar;
    }
}
