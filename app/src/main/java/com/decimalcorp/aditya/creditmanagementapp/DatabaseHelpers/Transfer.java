package com.decimalcorp.aditya.creditmanagementapp.DatabaseHelpers;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "transfers_table")
public class Transfer {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int key;

    @ColumnInfo(name = "Time")
    public String time;

    @ColumnInfo(name = "From")
    public String name1;

    @ColumnInfo(name = "To")
    public String name2;

    @ColumnInfo(name = "Amount")
    public int amount;

    public Transfer(@NonNull String time, String from, String to, int amount){
        this.time = time;
        this.name1 = from;
        this.name2 = to;
        this.amount = amount;
    }

    Transfer() {}

    public int getAmount() {
        return amount;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getTime() {
        return time;
    }
}
